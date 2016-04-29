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

import de.tumitfahrer.daos.ISearchDao;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.Search;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.LatLongUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService extends AbstractService implements EntityService<Search> {

    @Autowired
    private ISearchDao searchDao;

    public List<Ride> find(Search search) {
        search.setCreatedAt(DateUtils.getCurrentDate());
        search.setUpdatedAt(DateUtils.getCurrentDate());

        this.create(search);

        List<Ride> rides = null;
        // determine if we search for a place or lat/long
        if (search.hasLatLong()) {
            // Returns a larger set of rides which still need to be post processed
            rides = searchDao.findGeo(search);

            // Postprocessing
            List<Ride> filteredRides = new ArrayList<>();
            for (Ride ride : rides) {
                double distanceDeparture = LatLongUtil.getDistance(search.getDepartureLatitude(),
                        search.getDepartureLongitude(), ride.getDepartureLatitude(), ride.getDepartureLongitude());

                if (distanceDeparture > search.getDeparturePlaceThreshold()) {
                    continue;   // departure distance is too big
                }

                double distanceDestination = LatLongUtil.getDistance(search.getDestinationLatitude(),
                        search.getDestinationLongitude(), ride.getDestinationLatitude(),
                        ride.getDestinationLongitude());

                if (distanceDestination > search.getDestinationThreshold()) {
                    continue;   // destination distance is too big
                }

                filteredRides.add(ride);
            }

            rides = filteredRides;

        } else {
            // perform the existing search by comparing the departure and destination strings
            rides = searchDao.find(search);
        }

        return rides;
    }

    @Override
    public ServiceResponse<Search> create(Search search) {
        ServiceResponse<Search> serviceResponse = ServiceResponseFactory.createInstance();
        searchDao.save(search);
        serviceResponse.setEntity(search);
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Search> update(Search object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Search> delete(Search object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Search> delete(Integer id) {
        throw new NotImplementedException("TODO");
    }


    public Integer getSearchesCounter(Integer userID, String date, Boolean isOwner) {

        Date fromDate = DateUtils.parseQueryString(date);
        if (fromDate == null) {
            fromDate = DateUtils.getCurrentDate();
        }

        return searchDao.getSearchesCounter(userID, fromDate, isOwner);
    }

    public List<Search> getLastSearches() {
        return searchDao.getLastSearches();
    }
}