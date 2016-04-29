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

import de.tumitfahrer.daos.IRatingDao;
import de.tumitfahrer.entities.Rating;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService extends AbstractService implements EntityService<Rating> {

    @Autowired
    private IRatingDao ratingDao;

    public ServiceResponse<List<Rating>> getRatingsFromUserId(Integer userId) {
        ServiceResponse<List<Rating>> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(ratingDao.getRatingsFromUserId(userId));
        return serviceResponse;
    }

    public ServiceResponse<List<Rating>> getRatingsToUserId(Integer userId) {
        ServiceResponse<List<Rating>> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(ratingDao.getRatingsToUserId(userId));
        return serviceResponse;
    }

    public ServiceResponse<List<Rating>> getByRideId(Integer rideId) {
        ServiceResponse<List<Rating>> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(ratingDao.getByRideId(rideId));
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Rating> create(Rating rating) {
        ServiceResponse<Rating> serviceResponse = ServiceResponseFactory.createInstance();

        // check inputs
        if (rating.getToUserId() == null || rating.getToUserId() <= 0) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.invalid_id"));
        }

        if (rating.getRideId() == null || rating.getRideId() <= 0) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.invalid_id"));
        }

        if (rating.getRatingType() == null || rating.getRatingType() <= 0) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("rating.invalid_type"));
        }

        // stop if errors occurred
        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        rating.setCreatedAt(DateUtils.getCurrentDate());
        rating.setUpdatedAt(DateUtils.getCurrentDate());

        // insert and return
        ratingDao.saveRating(rating);
        serviceResponse.setEntity(rating);

        return serviceResponse;
    }

    @Override
    public ServiceResponse<Rating> update(Rating object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Rating> delete(Rating object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Rating> delete(Integer id) {
        throw new NotImplementedException("TODO");
    }
}