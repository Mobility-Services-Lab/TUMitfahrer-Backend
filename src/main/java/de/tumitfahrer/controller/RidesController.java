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

import com.wordnik.swagger.annotations.*;
import de.tumitfahrer.assembler.RideAssembler;
import de.tumitfahrer.dtos.http.ErrorResponseDTO;
import de.tumitfahrer.dtos.http.ResponseDTO;
import de.tumitfahrer.dtos.ride.response.RideDeletedResponseDTO;
import de.tumitfahrer.dtos.ride.response.RideIdsDTO;
import de.tumitfahrer.dtos.ride.response.RideResponseDTO;
import de.tumitfahrer.dtos.ride.response.RidesResponseDTO;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.RideService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rides")
@Api(value = "Ride Controller", description = "Ride specific functions")
public class RidesController extends AbstractController {

    @Autowired
    private RideService rideService;
    @Autowired
    private RideAssembler rideAssembler;

    @GET
    @Authenticated
    @ApiOperation("Returns all rides.")
    @ApiResponses({
            @ApiResponse(code = 200, response = RidesResponseDTO.class, message = "OK: alles in Ordnung"),
            @ApiResponse(code = 403, response = ErrorResponseDTO.class, message = "Fehler: nicht authorisiert"),
    })
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getAllRides(@QueryParam("page") Integer page, @QueryParam("ride_type") Integer rideType, @QueryParam("from_date") String fromDateString) {
        List<Ride> rides = rideService.getAllRides(page, rideType, fromDateString);
        RidesResponseDTO ridesResponseDTO = rideAssembler.toResponse(rides);
        return ResponseUtils.ok(ridesResponseDTO);
    }

    @GET
    @Authenticated
    @Path("/ids")
    @ApiOperation("Returns an array of all ride ids.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getAllRideIds() {
        List<Integer> rides = rideService.getAllRideIds();
        RideIdsDTO rideIdsDTO = new RideIdsDTO();
        rideIdsDTO.setIds(rides);
        return ResponseUtils.ok(rideIdsDTO);
    }

    @GET
    @Authenticated
    @Path("/{rideId}")
    @ApiOperation("Returns one specific ride claimed by the ride id.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getRideById(@PathParam("rideId") Integer rideId) {
        Ride ride = rideService.load(rideId);
        RideResponseDTO rideResponseDTO = rideAssembler.toResponse(ride);
        return ResponseUtils.ok(rideResponseDTO);
    }

    @GET
    @Authenticated
    @Path("/deleted/{rideId}")
    @ApiOperation("Returns one specific deleted ride claimed by the ride id.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getDeletedRideById(@PathParam("rideId") Integer rideId) {
        Ride ride = rideService.loadDeletedRide(rideId);
        RideDeletedResponseDTO rideResponseDTO = rideAssembler.toDeletedResponse(ride);
        return ResponseUtils.ok(rideResponseDTO);
    }

    @DELETE
    @Authenticated
    @Authorized
    @Path("/{rideId}")
    @ApiOperation("Deletes a given ride.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response deleteRide(@PathParam("rideId") Integer rideId) {
        rideService.delete(rideId);
        return ResponseUtils.ok(new ResponseDTO());
    }
}