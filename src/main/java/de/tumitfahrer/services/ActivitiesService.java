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

import de.tumitfahrer.dtos.activity.BadgeDTO;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivitiesService extends AbstractService {

    @Autowired
    private RideService rideService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private SearchService searchService;

    public ServiceResponse<BadgeDTO> getBadge(Integer userID, String campusUpdatedAt, String activityUpdatedAt,
                                              String timelineUpdatedAt, String ridesUpdatedAt) {

        Integer campusCounter, activityCounter, rideSearchesCounter, requestsCounter, newPassengers, newRequest, timelineBadge, myRides;

        Date campusUpdatedAtDate = DateUtils.parseDatetime(campusUpdatedAt);  //TODO cua.substring(1, cua.length()-1);
        if (campusUpdatedAtDate == null) {
            campusUpdatedAtDate = DateUtils.getCurrentDate();
            campusUpdatedAt = campusUpdatedAtDate.toString();
        }

        Date activityUpdatedAtDate = DateUtils.parseDatetime(activityUpdatedAt);
        if (activityUpdatedAtDate == null) {
            activityUpdatedAtDate = DateUtils.getCurrentDate();
            activityUpdatedAt = activityUpdatedAtDate.toString();
        }

        Date ridesUpdatedAtDate = DateUtils.parseDatetime(ridesUpdatedAt);
        if (ridesUpdatedAtDate == null) {
            ridesUpdatedAtDate = DateUtils.getCurrentDate();
            ridesUpdatedAt = ridesUpdatedAtDate.toString();
        }

        Date timelineUpdatedAtDate = DateUtils.parseDatetime(timelineUpdatedAt);
        if (timelineUpdatedAtDate == null) {
            timelineUpdatedAtDate = DateUtils.getCurrentDate();
            timelineUpdatedAt = timelineUpdatedAtDate.toString();
        }

        List<Ride> rides = rideService.getRidesByOwnerAndDate(userID, 0, campusUpdatedAt, false);
        campusCounter = rides.size();

        List<Ride> rides2 = rideService.getRidesByOwnerAndDate(userID, 1, activityUpdatedAt, false);
        activityCounter = rides2.size();

        rideSearchesCounter = searchService.getSearchesCounter(userID, timelineUpdatedAt, false);
        requestsCounter = requestService.getRequestsCounter(userID, timelineUpdatedAt, false);

        timelineBadge = campusCounter + activityCounter + rideSearchesCounter + requestsCounter;

        newPassengers = rideService.countNewPassenger(userID, ridesUpdatedAt);
        newRequest = requestService.getRequestsCounter(userID, ridesUpdatedAt, true);
        myRides = newPassengers + newRequest;

        BadgeDTO badge = new BadgeDTO(campusCounter, activityCounter, timelineBadge, myRides, campusUpdatedAt, activityUpdatedAt, timelineUpdatedAt, ridesUpdatedAt);

        ServiceResponse<BadgeDTO> badgeResponse = ServiceResponseFactory.createInstance();
        badgeResponse.setEntity(badge);
        return badgeResponse;
    }
}
