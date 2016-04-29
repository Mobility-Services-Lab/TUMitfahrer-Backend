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
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import de.tumitfahrer.assembler.RideAssembler;
import de.tumitfahrer.dtos.ride.request.UserRideRequestDTO;
import de.tumitfahrer.dtos.ride.response.RideResponseDTO;
import de.tumitfahrer.dtos.ride.response.RidesResponseDTO;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.ConversationService;
import de.tumitfahrer.services.PassengerService;
import de.tumitfahrer.services.RideService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{userId}/rides")
@Api(value = "User Ride Controller", description = "User Ride Management.")
public class UserRidesController extends AbstractController {

    @Autowired
    private RideService rideService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private RideAssembler rideAssembler;

    @GET
    @Authenticated
    @Authorized
    @ApiOperation("Returns all rides with applied filter.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getAllRides(@PathParam("userId") Integer userId, @QueryParam("driver") boolean driver, @QueryParam("passenger") boolean passenger, @QueryParam("past") boolean past, @QueryParam("future") boolean future) {
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }
        List<Ride> rides = rideService.getRidesByUserId(userId, driver, passenger, past, future);
        RidesResponseDTO ridesResponseDTO = rideAssembler.toResponse(rides);
        return ResponseUtils.ok(ridesResponseDTO);
    }

    @POST
    @Authenticated
    @Authorized
    @ApiOperation("Creates a ride for a user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response createRide(@PathParam("userId") Integer userId, UserRideRequestDTO userRideRequestDTO) {
        Ride ride = rideAssembler.toEntity(userRideRequestDTO);
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }
        ride.setUser(user);
        ServiceResponse<Ride> serviceResponse = rideService.create(ride, userRideRequestDTO.getRide().isDriver());

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        ride = serviceResponse.getEntity();
        RideResponseDTO rideResponseDTO = rideAssembler.toResponse(ride);
        rideResponseDTO.getRide().setConversationId(conversationService.getByRideId(ride.getId()).getEntity().getId());
        return ResponseUtils.ok(rideResponseDTO);
    }

    @DELETE
    @Authenticated
    @Authorized
    @Path("/{rideId}")
    @ApiOperation("Leave an already accepted ride.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response leaveRide(@PathParam("userId") Integer userId, @PathParam("rideId") Integer rideId) {
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }
        ServiceResponse<Ride> serviceResponse = passengerService.leaveRide(userId, rideId, true);
        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }
        return ResponseUtils.ok();
    }

    @PUT
    @Authenticated
    @Authorized
    @Path("/{rideId}")
    @ApiOperation("Remove a passenger from an accepted ride.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response removePassenger(@PathParam("userId") Integer userId, @PathParam("rideId") Integer rideId) {
        ServiceResponse<Ride> serviceResponse = passengerService.leaveRide(userId, rideId, false);
        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }
        return ResponseUtils.ok();
    }


    @PUT
    @Authenticated
    @Path("/offer/{rideId}")
    @ApiOperation("Offer ride to a ride request")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response offerRide(@PathParam("userId") Integer userId, @PathParam("rideId") Integer rideId) {
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }
        ServiceResponse<Ride> serviceResponse = rideService.offerRide(userId, rideId);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        Ride ride = serviceResponse.getEntity();
        RideResponseDTO rideResponseDTO = rideAssembler.toResponse(ride);
        return ResponseUtils.ok(rideResponseDTO);
    }
}