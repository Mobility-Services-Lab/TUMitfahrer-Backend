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
import de.tumitfahrer.dtos.user.request.UpdateUserDTO;
import de.tumitfahrer.entities.Request;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.enums.Language;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService extends AbstractService implements EntityService<User> {

    @Autowired
    private IUserDao userDao;
    @Autowired
    private MailService mailService;

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private RideService rideService;
    @Autowired
    private RequestService requestService;

    @Value("${api.key.validity}")
    private Integer apiKeyValidity;

    @Value("${valid.mails}")
    private String validMails;


    public User load(Integer userId) {
        return userDao.load(userId);
    }

    public User loadByEmail(String email) {
        return userDao.getUser(email);
    }

    public User loadByApiKey(String apiKey) {
        return userDao.getUserByApiKey(apiKey);
    }

    public Integer getUserIdByApiKey(String apiKey) {
        User user = userDao.getUserByApiKey(apiKey);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    public User generateApiKey(User user) {
        String apiKey = UUID.randomUUID().toString();
        user.setApiKey(apiKey);
        user.setApiKeyExpires(DateUtils.getCurrentDatePlusOffsetInHours(apiKeyValidity));
        userDao.update(user);
        return user;
    }


    public boolean isApiKeyExpired(User user) {
        return user.getApiKey() == null || user.getApiKeyExpires() == null || user.getApiKeyExpires().before(DateUtils.getCurrentDate());
    }

    public void resetApiKeyExpiry(User user) {
        user.setApiKeyExpires(DateUtils.getCurrentDatePlusOffsetInHours(apiKeyValidity));
        userDao.update(user);
    }

    public Integer getRidesAsDriverCount(Integer userId) {
        return userDao.getRidesAsDriverCount(userId);
    }

    public Integer getRidesAsPassengerCount(Integer userId) {
        return userDao.getRidesAsPassengerCount(userId);
    }

    @Override
    public ServiceResponse<User> create(User user) {

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();
            serviceResponse.getErrors().addConstraintViolations(violations);
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // set required fields
        String password = PasswordUtils.generatePassword();
        String salt = PasswordUtils.generateSalt();
        String sha_password = PasswordUtils.sha512(password + salt);

        user.setPassword(sha_password);
        user.setSalt(salt);

        user.setAdmin(false);
        user.setEnabled(true);
        user.setStudent(true);
        user.setEmail(user.getEmail().toLowerCase());

        user.setUpdatedAt(DateUtils.getCurrentDate());
        user.setCreatedAt(DateUtils.getCurrentDate());

        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();

        // insert and return
        try {
            userDao.save(user);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("index_users_on_email")) {
                // unique key on users caused this exception
                serviceResponse.getErrors().add(errorMsgsUtil.get("user.email.duplicate"));
                serviceResponse.getErrors().setStatus(HttpStatus.CONFLICT);
            } else {
                // unkown error
                serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            }

            return serviceResponse;
        }


        serviceResponse.setEntity(user);

        mailService.welcomeMail(user.getEmail(), user.getFirstName(), password, Language.EN);

        return serviceResponse;
    }

    public ServiceResponse<User> update(User user, UpdateUserDTO updateUserDTO) {

        if (updateUserDTO.getCar() != null) {
            user.setCar(updateUserDTO.getCar());
        }

        if (updateUserDTO.getDepartment() != null) {
            user.setDepartment(updateUserDTO.getDepartment());
        }

        if (updateUserDTO.getFirstName() != null) {
            user.setFirstName(updateUserDTO.getFirstName());
        }

        if (updateUserDTO.getLastName() != null) {
            user.setLastName(updateUserDTO.getLastName());
        }

        if (updateUserDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }

        if (updateUserDTO.getIntendedUse() != null) {
            user.setIntendedUse(updateUserDTO.getIntendedUse());
        }

        return update(user);
    }


    @Override
    public ServiceResponse<User> update(User user) {

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();
            serviceResponse.getErrors().addConstraintViolations(violations);
            return serviceResponse;
        }

        // update and return
        user.setUpdatedAt(DateUtils.getCurrentDate());

        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();
        try {
            userDao.update(user);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("index_users_on_email")) {
                // unique key on users caused this exception
                serviceResponse.getErrors().add(errorMsgsUtil.get("user.email.duplicate"));
                serviceResponse.getErrors().setStatus(HttpStatus.CONFLICT);
            } else {
                // unkown error
                serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            }

            return serviceResponse;
        }

        // update and return
        user.setUpdatedAt(DateUtils.getCurrentDate());
        userDao.update(user);
        serviceResponse.setEntity(user);

        return serviceResponse;
    }

    @Override
    public ServiceResponse<User> delete(final User user) {
        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();

        if (user == null) {
            serviceResponse.getErrors().add("User not found.");
            return serviceResponse;
        }

        final List<Ride> ownedRides = rideService.getRidesByUserId(user.getId(), true, true, false, true);

        // delete all rides that is the owner and notify accepted passengers
        for (Ride ownedRide : ownedRides) {
            rideService.delete(ownedRide);
        }

        // leave from accepted future rides
        final List<Ride> joinedRides = passengerService.getFutureRidesByUserId(user.getId());
        for (Ride joinedRide : joinedRides) {
            passengerService.leaveRide(user.getId(), joinedRide.getId(), true);
        }

        // remove requests from rides that are pending to be approved
        final List<Request> pendingRequests = requestService.getRequestsByUserId(user.getId());
        for (Request pendingRequest : pendingRequests) {
            requestService.delete(pendingRequest);
        }

        userDao.delete(user);

        return serviceResponse;
    }

    @Override
    public ServiceResponse<User> delete(Integer id) {
        return delete(userDao.load(id));
    }

    public ServiceResponse<User> loadDeletedUser(final Integer userId) {
        final ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();
        final User user = userDao.loadDeletedUser(userId);

        if (user == null) {
            serviceResponse.getErrors().add("This is not a deleted user.");
            serviceResponse.getErrors().setStatus(HttpStatus.NOT_FOUND);
            return serviceResponse;
        }

        serviceResponse.setEntity(user);
        return serviceResponse;
    }

    public ServiceResponse<User> invalidateApiKey(final User user) {

        user.setApiKey(null);
        user.setApiKeyExpires(null);
        user.setUpdatedAt(DateUtils.getCurrentDate());

        userDao.update(user);

        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(user);

        return serviceResponse;
    }
}