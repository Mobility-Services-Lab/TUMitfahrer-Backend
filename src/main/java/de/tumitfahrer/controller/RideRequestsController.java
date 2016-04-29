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
import de.tumitfahrer.assembler.PassengerAssembler;
import de.tumitfahrer.assembler.RequestAssembler;
import de.tumitfahrer.dtos.http.ResponseDTO;
import de.tumitfahrer.dtos.request.request.AcceptPassengerRequestDTO;
import de.tumitfahrer.dtos.request.request.RideRequestDTO;
import de.tumitfahrer.dtos.request.response.RideRequestResponseDTO;
import de.tumitfahrer.dtos.request.response.RideRequestsResponseDTO;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Request;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.PassengerService;
import de.tumitfahrer.services.RequestService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rides/{rideId}/requests")
@Api(value = "Ride Requests Controller", description = "Passenger request management for rides.")
public class RideRequestsController extends AbstractController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestAssembler requestAssembler;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private PassengerAssembler passengerAssembler;

    @GET
    @Authenticated
    @Authorized
    @ApiOperation("Returns all requests for a specific ride.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getAllRequests(@PathParam("rideId") Integer rideId) {
        List<Request> requests = requestService.getRequestsByRideId(rideId);
        RideRequestsResponseDTO rideRequestsResponseDTO = requestAssembler.toResponse(requests);
        return ResponseUtils.ok(rideRequestsResponseDTO);
    }

    @POST
    @Authenticated
    @ApiOperation("Create a ride request.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response createRideRequest(@PathParam("rideId") Integer rideId, RideRequestDTO rideRequestDTO) {
        Request request = requestAssembler.toEntity(rideRequestDTO);
        request.setRideId(rideId);

        ServiceResponse<Request> serviceResponse = requestService.create(request);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        RideRequestResponseDTO rideRequestResponseDTO = requestAssembler.toResponse(request);
        return ResponseUtils.ok(rideRequestResponseDTO);
    }

    @PUT
    @Authenticated
    @Authorized
    @ApiOperation("Accept a ride request. Setting confirmed = false declines the passenger.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response acceptPassenger(@PathParam("rideId") Integer rideId, AcceptPassengerRequestDTO acceptPassengerRequestDTO) {
        ServiceResponse serviceResponse;
        Passenger passenger = passengerAssembler.toEntity(acceptPassengerRequestDTO);
        passenger.setRideId(rideId);
        passenger.setDriver(false);

        if (acceptPassengerRequestDTO.getConfirmed() == null || !acceptPassengerRequestDTO.getConfirmed()) {
            serviceResponse = requestService.decline(passenger.getRideId(), passenger.getUserId());
        } else {
            serviceResponse = passengerService.acceptPassenger(passenger);
        }

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        return ResponseUtils.ok();
    }

    @DELETE
    @Authenticated
    @Authorized
    @Path("/{requestId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    @ApiOperation("Deletes a ride request.")
    public Response deleteRequest(@PathParam("rideId") Integer rideId, @PathParam("requestId") Integer requestId) {
        requestService.delete(requestId);
        return ResponseUtils.ok(new ResponseDTO());
    }
}