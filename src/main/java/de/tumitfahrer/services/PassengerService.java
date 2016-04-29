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

import de.tumitfahrer.daos.IPassengerDao;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Request;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.SecurityUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService extends AbstractService implements EntityService<Passenger> {

    @Autowired
    private IPassengerDao passengerDao;
    @Autowired
    private RideService rideService;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private ConvParticipantsService convParticipantsService;

    public ServiceResponse<List<User>> getByRideId(Integer rideId) {
        ServiceResponse<List<User>> serviceResponse = ServiceResponseFactory.createInstance();

        List<User> passengers = new ArrayList<>();
        if (securityUtils.getCurrentUser() != null && (securityUtils.isAdmin() || securityUtils.userIsPassenger(rideId))) {
            passengers = passengerDao.getByRideId(rideId);
        }
        // else return empty list, cause user does not see passengers

        serviceResponse.setEntity(passengers);
        return serviceResponse;
    }

    public List<Ride> getByUserId(Integer userId) {
        List<Ride> rides = new ArrayList<>();
        rides = passengerDao.getByUserId(userId);
        return rides;
    }

    public List<Ride> getFutureRidesByUserId(Integer userId) {
        List<Ride> rides = new ArrayList<>();
        rides = passengerDao.getFutureRidesByUserId(userId);
        return rides;
    }

    public ServiceResponse<Passenger> acceptPassenger(Passenger passenger) {
        ServiceResponse<Passenger> serviceResponse = ServiceResponseFactory.createInstance();

        // check ride and user
        Ride ride = rideService.load(passenger.getRideId());
        List<User> passengers;
        if (ride == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.not_found"));
        } else if (ride.isPastRide()) {
            // the ride is already in the past
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.departure_time.past"));
        }

        User user = userService.load(passenger.getUserId());
        if (user == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.not_found"));
        }

        if (ride != null && user != null) {
            passengers = this.getByRideId(passenger.getRideId()).getEntity();
            if (passengers.size() >= ride.getFreeSeats() + 1) {        // Passengers must be at most driver + <free seats>)
                serviceResponse.getErrors().add(errorMsgsUtil.get("ride.full"));
            }
        }

        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // check for pending requests
        Request request = requestService.getRequestByRideAndUserId(passenger.getRideId(), passenger.getUserId());
        if (request == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("request.none_user"));
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        serviceResponse = create(passenger);

        if (serviceResponse.hasErrors()) {
            return serviceResponse;
        }

        // update request
        request.setAnsweredAt(DateUtils.getCurrentDate());
        requestService.update(request);

        //Notifications Section
        notificationService.notifyPassengerAdded(passenger.getUserId(), passenger.getRideId());
        convParticipantsService.addParticipant(passenger);
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Passenger> create(Passenger passenger) {
        ServiceResponse<Passenger> serviceResponse = ServiceResponseFactory.createInstance();

        // check ride and user
        Ride ride = rideService.load(passenger.getRideId());
        if (ride == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.not_found"));
        }

        User user = userService.load(passenger.getUserId());
        if (user == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.not_found"));
        }

        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // check passenger
        Passenger p = loadByRideAndUserId(passenger.getRideId(), passenger.getUserId());
        if (p != null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("passenger.exists_already"));
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // save passenger
        passenger.setCreatedAt(DateUtils.getCurrentDate());
        passenger.setUpdatedAt(DateUtils.getCurrentDate());
        passengerDao.save(passenger);

        serviceResponse.setEntity(passenger);
        return serviceResponse;
    }

    public Passenger loadByRideAndUserId(Integer rideId, Integer userId) {
        return passengerDao.loadByRideAndUserId(rideId, userId);
    }

    public ServiceResponse<Ride> leaveRide(Integer userId, Integer rideId, Boolean leftOnOwn) {
        ServiceResponse<Ride> serviceResponse = ServiceResponseFactory.createInstance();

        // parameter checks
        if (userId == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.invalid_id"));
        }

        if (rideId == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.invalid_id"));
        }

        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // passenger check
        Passenger passenger = loadByRideAndUserId(rideId, userId);
        if (passenger == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("passenger.not_in_ride"));
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        Request request = requestService.getRequestAnswered(rideId, userId);
        if (request != null) {
            requestService.delete(request); //Delete request to allow new ones
        }

        // driver check
        if (passenger.isDriver()) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("passenger.remove_driver"));
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        delete(passenger);

        //Notification
        if (leftOnOwn) {
            notificationService.notifyPassengerLeft(userId, rideId);
        } else {
            notificationService.notifyPassengerRemoved(userId, rideId);
        }
        //
        convParticipantsService.removeParticipant(passenger);
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Passenger> update(Passenger object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Passenger> delete(Passenger passenger) {
        if (passenger != null) {
            passengerDao.delete(passenger);
        }

        //Notification

        //

        return ServiceResponseFactory.createInstance();
    }

    @Override
    public ServiceResponse<Passenger> delete(Integer id) {
        return delete(passengerDao.load(id));
    }
}