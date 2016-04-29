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

import de.tumitfahrer.daos.IRideDao;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.services.RideService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RidesSecurity extends AbstractSecurity {

    @Autowired
    IRideDao rideDao;
    @Autowired
    RideService rideService;

    @SuppressWarnings("unused")
    public void deleteRide() {
        if (isAdmin() || userHasAccessOnRideId()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    private boolean userHasAccessOnRideId() {
        Integer rideId;
        try {
            rideId = Integer.parseInt(pathParams.getFirst("rideId"));
        } catch (ClassCastException e) {
            return false;
        }

        Ride ride = rideService.load(rideId);

        if (ride == null) {
            return false;
        }

        return currentUser.getId().equals(ride.getUser().getId());

    }
}
