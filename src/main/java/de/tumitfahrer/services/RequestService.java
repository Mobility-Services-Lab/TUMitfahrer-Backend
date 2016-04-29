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

import de.tumitfahrer.daos.IRequestDao;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Request;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.SecurityUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RequestService extends AbstractService implements EntityService<Request> {

    @Autowired
    private IRequestDao requestDao;
    @Autowired
    private RideService rideService;
    @Autowired
    private UserService userService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SecurityUtils securityUtils;

    public List<Request> getRequestsByRideId(Integer rideId) {
        if (securityUtils.getCurrentUser() == null) {
            return new ArrayList<>();
        }

        if (securityUtils.isAdmin() || securityUtils.userHasDriverAccessOnRideId(rideId)) {
            return requestDao.getRequestsByRideId(rideId);
        }

        return requestDao.getRequestsByRideId(rideId, Restrictions.eq("userId", securityUtils.getCurrentUser().getId()));
    }

    public List<Request> getRequestsByUserId(Integer userId) {
        return requestDao.getRequestsByUserId(userId);
    }

    public Request getRequestByRideAndUserId(Integer rideId, Integer userId) {
        return requestDao.loadByRideAndUserId(rideId, userId);
    }

    public ServiceResponse<Request> decline(Integer rideId, Integer userId) {
        ServiceResponse<Request> serviceResponse = ServiceResponseFactory.createInstance();

        // check ride and user
        final Ride ride = rideService.load(rideId);
        if (ride == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.not_found"));
        } else if (ride.isPastRide()) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.departure_time.past"));
        }

        final User user = userService.load(userId);
        if (user == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.not_found"));
        }

        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        return decline(ride, user);
    }

    public ServiceResponse<Request> decline(final Ride ride, final User user) {
        ServiceResponse<Request> serviceResponse = ServiceResponseFactory.createInstance();

        // check for pending requests
        Request request = getRequestByRideAndUserId(ride.getId(), user.getId());
        if (request == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("request.none_user"));
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // check passenger
        Passenger p = passengerService.loadByRideAndUserId(ride.getId(), user.getId());
        if (p != null) {
            passengerService.delete(p);
        }

        //Notifications
        notificationService.notifyRequestDeclined(user.getId(), ride.getId());

        return delete(request);
    }

    public Request load(Integer requestId) {
        return requestDao.load(requestId);
    }

    public Request getRequestAnswered(Integer rideId, Integer userId) {
        return requestDao.loadByRideAndUserIdAnswered(rideId, userId);
    }

    @Override
    public ServiceResponse<Request> create(final Request request) {
        final ServiceResponse<Request> serviceResponse = ServiceResponseFactory.createInstance();

        // check inputs
        Set<ConstraintViolation<Request>> violations = validator.validate(request);
        if (violations.size() > 0) {
            serviceResponse.getErrors().addConstraintViolations(violations);
        }

        final Ride ride = rideService.load(request.getRideId());
        if (ride.isPastRide()) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.departure_time.past"));
        }

        // stop if errors occurred
        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // check for existing request
        final Request r = requestDao.loadByRideAndUserIdIgnoreAnswered(request.getRideId(), request.getUserId());
        if (r != null) {
            if (r.getAnsweredAt() == null) {
                serviceResponse.getErrors().add(errorMsgsUtil.get("request.already_sent"));
            } else {
                serviceResponse.getErrors().add(errorMsgsUtil.get("request.already_accepted"));
            }
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        request.setCreatedAt(DateUtils.getCurrentDate());
        request.setUpdatedAt(DateUtils.getCurrentDate());

        // insert and return
        requestDao.save(request);
        serviceResponse.setEntity(request);

        //Notification Section
        notificationService.notifyRequestRide(request.getUserId(), request.getRideId());

        return serviceResponse;
    }

    @Override
    public ServiceResponse<Request> update(Request request) {

        Set<ConstraintViolation<Request>> violations = validator.validate(request);
        if (violations.size() > 0) {
            return ServiceResponseFactory.fromViolations(violations);
        }

        requestDao.update(request);

        ServiceResponse<Request> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(request);
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Request> delete(Request request) {
        if (request != null) {
            requestDao.delete(request);
        }
        return ServiceResponseFactory.createInstance();
    }

    @Override
    public ServiceResponse<Request> delete(Integer id) {
        return delete(requestDao.load(id));
    }

    public Integer getRequestsCounter(Integer userID, String fromDateString, Boolean isOwner) {
        Date fromDate = DateUtils.parseQueryString(fromDateString);
        if (fromDate == null) {
            fromDate = DateUtils.getCurrentDate();
        }
        return requestDao.getRequestsCounter(userID, fromDate, isOwner);

    }

    public List<Request> getLastRequests() {
        return requestDao.getLastRequests();
    }
}