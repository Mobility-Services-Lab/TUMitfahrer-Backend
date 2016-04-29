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

import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.services.PassengerService;
import de.tumitfahrer.services.RideService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRidesSecurity extends AbstractSecurity {

    @Autowired
    RideService rideService;
    @Autowired
    PassengerService passengerService;

    @SuppressWarnings("unused")
    public void getAllRides() {
        if (isAdmin() || isUserIdAuthorizedUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    @SuppressWarnings("unused")
    public void createRide() {
        if (isAdmin() || isUserIdAuthorizedUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    @SuppressWarnings("unused")
    public void leaveRide() {
        if (isAdmin() || (isUserIdAuthorizedUser() && isUserIdPassengerInRideId())) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    @SuppressWarnings("unused")
    public void removePassenger() {
        if (isAdmin() || userHasAccessOnRideId()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    private boolean isUserIdPassengerInRideId() {
        Integer rideId;
        Integer userId;
        try {
            rideId = Integer.parseInt(pathParams.getFirst("rideId"));
            userId = Integer.parseInt(pathParams.getFirst("userId"));
        } catch (ClassCastException e) {
            return false;
        }

        Passenger passenger = passengerService.loadByRideAndUserId(rideId, userId);
        return passenger != null;

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
