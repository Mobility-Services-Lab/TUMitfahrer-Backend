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
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Authenticated
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!isAuthenticated(requestContext)) {
            requestContext.abortWith(ResponseUtils.unauthorized());
        }
    }

    private boolean isAuthenticated(ContainerRequestContext requestContext) {
        String apiKey = requestContext.getHeaderString("Authorization");

        if (apiKey == null) {
            return false;
        }

        User user = userService.loadByApiKey(apiKey);
        if (user != null && !user.isDeleted() && !userService.isApiKeyExpired(user)) {
            userService.resetApiKeyExpiry(user);
            return true;
        }

        return false;
    }
}