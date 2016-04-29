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

package de.tumitfahrer.services;

import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.BaseAuthUtils;
import de.tumitfahrer.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Service
public class SessionService extends AbstractService {

    @Context
    SecurityContext securityContext;
    @Context
    ContainerRequestContext containerRequest;
    @Autowired
    private UserService userService;
    @Autowired
    private IUserDao userDao;

    public ServiceResponse<User> createSession(final String basicAuth) {
        final ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();

        if (basicAuth == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.no_authentication"));
            return serviceResponse;
        }

        if (!basicAuth.startsWith("Basic")) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_authentication"));
            return serviceResponse;
        }

        final ServiceResponse<User> authenticateUserResponse = findAndAuthenticateUser(basicAuth);
        if (authenticateUserResponse.hasErrors()) {
            return authenticateUserResponse;
        }

        User user = authenticateUserResponse.getEntity();

        if (!userService.isApiKeyExpired(user)) {
            // he is already logged in and has an active api key
            userService.resetApiKeyExpiry(user);
        } else {
            user = userService.generateApiKey(user);
        }

        serviceResponse.setEntity(user);
        return serviceResponse;
    }

    public ServiceResponse<User> destroySession(String apiKey) {
        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();
        User user = userDao.getUserByApiKey(apiKey);

        if (user == null) {
            // hide mail existence
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_credentials"));
            return serviceResponse;
        }

        return userService.invalidateApiKey(user);
    }

    /**
     * Extracts the credentials from the authentication string and tries to find and authenticate the
     * corresponding user.
     *
     * @param basicAuth Basic Authentication Hash
     * @return Authenticated user or error message
     */
    private ServiceResponse<User> findAndAuthenticateUser(final String basicAuth) {
        final ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();

        final String username = BaseAuthUtils.getUsername(basicAuth);
        final String tryPassword = BaseAuthUtils.getPassword(basicAuth);

        if (username == null || tryPassword == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_authentication"));
            return serviceResponse;
        }

        User user = userDao.getUser(username.toLowerCase());

        if (user == null) {
            // hide mail existence
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_credentials"));
            return serviceResponse;
        }

        boolean isAuthenticated = PasswordUtils.isAuthenticated(user, tryPassword);

        if (!isAuthenticated) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_credentials"));
            return serviceResponse;
        }

        serviceResponse.setEntity(user);
        return serviceResponse;
    }
}