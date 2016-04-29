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

package de.tumitfahrer.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import de.tumitfahrer.assembler.RequestAssembler;
import de.tumitfahrer.assembler.RideAssembler;
import de.tumitfahrer.assembler.SearchAssembler;
import de.tumitfahrer.dtos.activity.ActivityDTO;
import de.tumitfahrer.dtos.activity.ActivityResponseDTO;
import de.tumitfahrer.dtos.activity.BadgeDTO;
import de.tumitfahrer.dtos.activity.BadgeResponseDTO;
import de.tumitfahrer.dtos.request.response.RideRequestEntityDTO;
import de.tumitfahrer.dtos.ride.response.RideDTO;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.*;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/activities")
@Api(value = "Activities Controller", description = "Activities specific functions")
public class ActivitiesController extends AbstractController {

    @Autowired
    private RideService rideService;
    @Autowired
    private RideAssembler rideAssembler;
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestAssembler requestAssembler;
    @Autowired
    private SearchService searchService;
    @Autowired
    private SearchAssembler searchAssembler;
    @Autowired
    private ActivitiesService activitiesService;
    @Autowired
    private UserService userService;

    @GET
    @ApiOperation("Overview of last activities, requests and searches")
    public Response getAllActivities() {
        List<RideDTO> ridesDTO = rideAssembler.toDto(rideService.getRidesByFutureDeparture());
        List<RideRequestEntityDTO> rideRequestsDTO = requestAssembler.toDto(requestService.getLastRequests());

        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setRides(ridesDTO);
        activityDTO.setRequests(rideRequestsDTO);

        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO();
        activityResponseDTO.setActivities(activityDTO);

        return ResponseUtils.ok(activityResponseDTO);
    }


    // GET /api/v2/activities/badges?campus_updated_at='2012-02-03 12:30'&user_id=id
    @GET
    @ApiOperation("Get badges for user events")
    @Path("/badges")
    public Response getBadgeCounter(@QueryParam("user_id") Integer userID, @QueryParam("campus_updated_at") String campusUpdatedAt, @QueryParam("activity_updated_at") String activityUpdatedAt,
                                    @QueryParam("timeline_updated_at") String timelineUpdatedAt, @QueryParam("my_rides_updated_at") String ridesUpdatedAt) {
        User user = userService.load(userID);
        if (user == null) {
            return ResponseUtils.notFound("User Not Found");
        }
        ServiceResponse<BadgeDTO> serviceResponse = activitiesService.getBadge(userID, campusUpdatedAt, activityUpdatedAt, timelineUpdatedAt, ridesUpdatedAt);
        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        BadgeResponseDTO br = new BadgeResponseDTO();
        br.setBadge(serviceResponse.getEntity());
        return Response.ok(br).build();
    }
}
