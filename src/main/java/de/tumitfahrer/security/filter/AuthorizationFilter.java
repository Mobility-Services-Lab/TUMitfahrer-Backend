/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.security.filter;

import de.tumitfahrer.entities.User;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.security.controller.AbstractSecurity;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Provider
@Authorized
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    @Autowired
    private UserService userService;
    private ApplicationContext applicationContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        setApplicationContext(ContextLoader.getCurrentWebApplicationContext());

        // fetch security class
        String controller = resourceInfo.getResourceClass().getSimpleName();
        AbstractSecurity security = getSecurityClass(controller);

        if (security == null) {
            requestContext.abortWith(ResponseUtils.serviceUnavailable("No valid ObjectSecurity found. Check @Authorized annotation!"));
            return;
        }

        // fill security class
        User currentUser = userService.loadByApiKey(requestContext.getHeaderString("Authorization"));
        security.init(requestContext, currentUser, requestContext.getUriInfo().getPathParameters());

        // dynamic method call in _Security class where _ is any _Controller class
        try {
            Method method = security.getClass().getMethod(resourceInfo.getResourceMethod().getName());
            method.invoke(security);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            requestContext.abortWith(ResponseUtils.serviceUnavailable("No valid MethodSecurity found. Check @Authorized annotation! Exception: " + e.getMessage()));
        } catch (InvocationTargetException e) {
            requestContext.abortWith(ResponseUtils.serviceUnavailable(e.getTargetException().toString()));
        }
    }

    private AbstractSecurity getSecurityClass(String controller) {
        String securityClassName = controller.substring(0, 1).toLowerCase() +
                controller.substring(1).replace("Controller", "Security");
        try {
            return (AbstractSecurity) applicationContext.getBean(securityClassName);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}