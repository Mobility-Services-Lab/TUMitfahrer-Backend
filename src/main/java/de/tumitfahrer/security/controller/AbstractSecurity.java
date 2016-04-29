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

package de.tumitfahrer.security.controller;

import de.tumitfahrer.entities.User;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

public abstract class AbstractSecurity {

    protected ContainerRequestContext requestContext;
    protected User currentUser;
    protected MultivaluedMap<String, String> pathParams;

    public void init(ContainerRequestContext requestContext, User currentUser, MultivaluedMap<String, String> pathParams) {
        this.requestContext = requestContext;
        this.currentUser = currentUser;
        this.pathParams = pathParams;
    }

    public boolean isEnabled() {
        return currentUser != null && currentUser.isEnabled();
    }

    public boolean isUserIdAuthorizedUser() {
        Integer userId;
        try {
            userId = Integer.parseInt(pathParams.getFirst("userId"));
        } catch (ClassCastException e) {
            return false;
        }

        return currentUser != null && currentUser.getId().equals(userId) && currentUser.getDeletedAt() == null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin() != null && currentUser.isAdmin();
    }
}
