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

import de.tumitfahrer.entities.Device;
import de.tumitfahrer.services.DeviceService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevicesSecurity extends AbstractSecurity {

    @Autowired
    DeviceService deviceService;

    @SuppressWarnings("unused")
    public void getAllDevicesByUserId() {
        if (isAdmin() || isUserIdAuthorizedUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    @SuppressWarnings("unused")
    public void createDevice() {
        if (isAdmin() || isUserIdAuthorizedUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    @SuppressWarnings("unused")
    public void deleteDevice() {
        if (isAdmin() || (isUserIdAuthorizedUser() && userHasAccessOnDeviceId())) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }


    private boolean userHasAccessOnDeviceId() {
        Integer deviceId;
        try {
            deviceId = Integer.parseInt(pathParams.getFirst("deviceId"));
        } catch (ClassCastException e) {
            return false;
        }

        Device device = deviceService.load(deviceId);

        if (device == null) {
            return false;
        }

        return currentUser.getId().equals(device.getUserId());

    }
}
