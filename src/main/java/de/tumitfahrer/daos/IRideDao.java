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

package de.tumitfahrer.daos;


import de.tumitfahrer.entities.Ride;

import java.util.Date;
import java.util.List;

public interface IRideDao extends IGenericDao<Ride> {

    List<Ride> getAllRides(Integer page, Date fromDate);

    List<Ride> getAllRides(Integer page, Integer rideType, Date fromDate);

    List<Integer> getAllRideIds();

    List<Ride> getRidesByUserId(Integer userId);

    List<Ride> getRidesByUserId(Integer userId, boolean driver, boolean passenger, boolean past, boolean future);

    List<Ride> getRidesByOwnerAndDate(Integer userId, Integer rideType, Date fromDate, Boolean isOwner);

    List<Ride> getRidesByDeparture(Date fromDate);

    Boolean isRideRequest(Integer rideId);

    Integer getPassengerCount(Integer rideId);

    Integer newPassengerCount(Integer userId, Date fromDate);

    Ride loadDeletedRide(Integer rideId);

}