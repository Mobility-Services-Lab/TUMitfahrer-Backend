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

package de.tumitfahrer.util;

import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.PassengerService;
import de.tumitfahrer.services.RideService;
import de.tumitfahrer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SecurityUtils {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private RideService rideService;
    @Autowired
    private PassengerService passengerService;

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getAuthHeader() {
        return request.getHeader("Authorization");
    }

    public User getCurrentUser() {
        String auth = getAuthHeader();
        if (auth == null || auth.equals("")) {
            return null;
        }
        return userService.loadByApiKey(auth);
    }

    public Ride getCurrentRide() {
        return rideService.load(getRideId());
    }

    public boolean isAdmin() {
        return getCurrentUser() != null && getCurrentUser().isAdmin() != null && getCurrentUser().isAdmin();
    }

    public boolean userHasDriverAccessOnRideId() {
        Integer rideId;
        try {
            rideId = getRideId();
        } catch (ClassCastException e) {
            return false;
        }
        return userHasDriverAccessOnRideId(rideId);
    }

    public boolean userHasDriverAccessOnRideId(Integer rideId) {
        Ride ride = rideService.load(rideId);

        if (ride == null) {
            return false;
        }

        return getCurrentUser() != null && getCurrentUser().getId().equals(ride.getUser().getId());

    }

    public boolean userIsPassenger() {
        Integer rideId;
        try {
            rideId = getRideId();
        } catch (ClassCastException e) {
            return false;
        }
        return userIsPassenger(rideId);
    }

    public boolean userIsPassenger(Integer rideId) {
        if (userHasDriverAccessOnRideId(rideId)) {
            return true;
        }

        if (getCurrentUser() == null) {
            return false;
        }

        Passenger p = passengerService.loadByRideAndUserId(rideId, getCurrentUser().getId());
        return p != null;
    }

    private int getRideId() {
        String path = request.getPathInfo();
        Pattern pattern = Pattern.compile("/rides/([0-9]+)");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }
}
