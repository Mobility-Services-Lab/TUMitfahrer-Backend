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

import de.tumitfahrer.daos.IRideDao;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.entities.validation.CreateRideGroup;
import de.tumitfahrer.entities.validation.RideGroup;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.LatLongUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RideService extends AbstractService implements EntityService<Ride> {

    @Autowired
    private IRideDao rideDao;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;

    public Ride load(Integer rideId) {
        return rideDao.load(rideId);
    }

    public List<Ride> getAllRides(Integer page, Integer rideType, String fromDateString) {
        if (page == null || page < 0) {
            page = 0;
        }

        Date fromDate = DateUtils.parseQueryString(fromDateString);

        // Default date is the current date, also if a parser error occurs.
        if (fromDate == null || fromDateString == null) {
            fromDate = DateUtils.getCurrentDate();
        }

        if (rideType == null) {
            return rideDao.getAllRides(page, fromDate);
        }
        if (rideType > 0) {
            return rideDao.getAllRides(page, 1, fromDate);
        }
        return rideDao.getAllRides(page, 0, fromDate);
    }

    public List<Integer> getAllRideIds() {
        return rideDao.getAllRideIds();
    }

    public List<Ride> getRidesByUserId(Integer userId, boolean driver, boolean passenger, boolean past, boolean future) {
        return rideDao.getRidesByUserId(userId, driver, passenger, past, future);
    }


    /**
     * This method creates a ride and additionally adds a driver/passenger
     *
     * @param ride
     * @param isDriver
     * @return
     */
    public ServiceResponse<Ride> create(Ride ride, boolean isDriver) {
        ServiceResponse<Ride> serviceResponse = create(ride);

        if (serviceResponse.hasErrors()) {
            return serviceResponse;
        }

        Passenger passenger = new Passenger();
        passenger.setUserId(ride.getUser().getId());
        passenger.setRideId(ride.getId());
        passenger.setDriver(isDriver);
        passenger.setCreatedAt(ride.getCreatedAt());
        passenger.setUpdatedAt(ride.getUpdatedAt());
        passengerService.create(passenger);

        serviceResponse.setEntity(load(ride.getId()));

        return serviceResponse;
    }

    @Override
    public ServiceResponse<Ride> create(Ride ride) {
        ServiceResponse<Ride> serviceResponse = ServiceResponseFactory.createInstance();

        Set<ConstraintViolation<Ride>> violations = validator.validate(ride, Default.class, CreateRideGroup.class);
        if (violations.size() > 0) {
            serviceResponse.getErrors().addConstraintViolations(violations);
        }

        // ########################################################################################
        // FIXME: should be moved to validator
        if (!LatLongUtil.isInLatitudeBounds(ride.getDepartureLatitude())
                || !LatLongUtil.isInLongitudeBounds(ride.getDepartureLongitude())) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.departureLatLong.invalid"));
        }

        if (!LatLongUtil.isInLatitudeBounds(ride.getDestinationLatitude())
                || !LatLongUtil.isInLongitudeBounds(ride.getDestinationLongitude())) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.destinationLatLong.invalid"));
        }
        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }
        // ########################################################################################


        ride.setCreatedAt(DateUtils.getCurrentDate());
        ride.setUpdatedAt(DateUtils.getCurrentDate());

        rideDao.save(ride);

        serviceResponse.setEntity(ride);
        conversationService.create(ride);
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Ride> update(Ride object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Ride> delete(Ride ride) {
        if (ride != null) {
            notificationService.notifyRideCanceled(ride.getId());
            conversationService.delete(ride);
            rideDao.delete(ride);
        }
        return null;
    }

    @Override
    public ServiceResponse<Ride> delete(Integer id) {
        return delete(rideDao.load(id));
    }

    public List<Ride> getRidesByOwnerAndDate(Integer userID, Integer rideType, String fromDateString, Boolean isOwner) {

        Date fromDate = DateUtils.parseQueryString(fromDateString);
        if (fromDate == null) {
            fromDate = DateUtils.getCurrentDate();
        }

        return rideDao.getRidesByOwnerAndDate(userID, rideType, fromDate, isOwner);
    }

    public Integer countNewPassenger(Integer userID, String fromDateString) {
        Date fromDate = DateUtils.parseQueryString(fromDateString);
        if (fromDate == null) {
            fromDate = DateUtils.getCurrentDate();
        }
        return rideDao.newPassengerCount(userID, fromDate);
    }

    public List<Ride> getRidesByFutureDeparture() {
        return rideDao.getRidesByDeparture(DateUtils.getCurrentDate());
    }

    public Boolean isRideRequest(Integer rideID) {
        return rideDao.isRideRequest(rideID);
    }

    public int getCurrentFreeSeats(Ride ride) {
        Integer passengers = rideDao.getPassengerCount(ride.getId());
        return ride.getFreeSeats() - passengers;
    }

    public Ride loadDeletedRide(Integer rideId) {
        return rideDao.loadDeletedRide(rideId);
    }

    public ServiceResponse<Ride> offerRide(Integer userId, Integer rideId) {
        ServiceResponse<Ride> serviceResponse = ServiceResponseFactory.createInstance();

        Ride ride = rideDao.load(rideId);
        if (ride == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.not_found"));
        }
        if (!isRideRequest(rideId)) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.not_requested_ride"));
        }

        Set<ConstraintViolation<Ride>> violations = validator.validate(ride, Default.class, RideGroup.class);
        if (violations.size() > 0) {
            serviceResponse.getErrors().addConstraintViolations(violations);
        }


        User offerer = userService.load(userId);
        if (offerer == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.not_found"));
        }

        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }


        User requester = ride.getUser();

        ride.setUser(offerer);
        ride.setFreeSeats(3);                               //TODO Temporarily add seats to the offered ride : Change it
        ride.setUpdatedAt(DateUtils.getCurrentDate());

        Passenger driver = createOfferDriver(offerer, ride);
        passengerService.create(driver);

        rideDao.update(ride);

        //Notifications
        notificationService.notifyOfferedRide(requester.getId(), ride.getId());
        //

        ride = rideDao.load(rideId);
        serviceResponse.setEntity(ride);
        return serviceResponse;
    }

    private Passenger createOfferDriver(User user, Ride ride) {
        Passenger passenger = new Passenger();
        passenger.setRideId(ride.getId());
        passenger.setDriver(true);
        passenger.setUserId(user.getId());
        return passenger;

    }
}