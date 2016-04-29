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
import de.tumitfahrer.assembler.RequestAssembler;
import de.tumitfahrer.assembler.UniversityDepartmentAssembler;
import de.tumitfahrer.assembler.UserAssembler;
import de.tumitfahrer.dtos.enums.UniversityDepartmentResponseDTO;
import de.tumitfahrer.dtos.http.ResponseDTO;
import de.tumitfahrer.dtos.request.response.RideRequestsResponseDTO;
import de.tumitfahrer.dtos.user.request.CreateUserRequestDTO;
import de.tumitfahrer.dtos.user.request.UpdateUserRequestDTO;
import de.tumitfahrer.dtos.user.response.UserDeletedResponseDTO;
import de.tumitfahrer.dtos.user.response.UserResponseDTO;
import de.tumitfahrer.entities.Request;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.enums.UniversityDepartment;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.security.annotations.CacheMaxAge;
import de.tumitfahrer.services.RequestService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Path("/users")
@Api(value = "User Controller", description = "User management interface.")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAssembler userAssembler;
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestAssembler requestAssembler;
    @Autowired
    private UniversityDepartmentAssembler universityDepartmentAssembler;

    @GET
    @Authenticated
    @Path("/{userId}")
    @ApiOperation("Returns one specific user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getUser(@PathParam("userId") Integer userId) {
        User user = userService.load(userId);

        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }

        UserResponseDTO userResponseDTO = userAssembler.toResponse(user);
        return ResponseUtils.ok(userResponseDTO);
    }

    @PUT
    @Authenticated
    @Authorized
    @Path("/{userId}")
    @ApiOperation("Updates a user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response updateUser(@PathParam("userId") Integer userId, UpdateUserRequestDTO updateUserRequestDTO) {
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }
        ServiceResponse<User> serviceResponse = userService.update(user, updateUserRequestDTO.getUser());

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        user = serviceResponse.getEntity();
        UserResponseDTO userResponseDTO = userAssembler.toResponse(user);
        return ResponseUtils.ok(userResponseDTO);
    }

    @POST
    @ApiOperation("Creates a new user.")
    public Response createUser(@Valid CreateUserRequestDTO createUserRequestDTO) {
        User user = mapper.map(createUserRequestDTO.getUser(), User.class);

        ServiceResponse<User> serviceResponse = userService.create(user);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        user = serviceResponse.getEntity();
        UserResponseDTO userResponseDTO = userAssembler.toResponse(user);
        return ResponseUtils.ok(userResponseDTO);
    }

    @GET
    @Authenticated
    @Path("{userId}/requests")
    @ApiOperation("Lists all requests of a specific user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getUserRequests(@PathParam("userId") Integer userId) {
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User not found");
        }
        List<Request> requests = requestService.getRequestsByUserId(userId);
        RideRequestsResponseDTO rideRequestsResponseDTO = requestAssembler.toResponse(requests);
        return ResponseUtils.ok(rideRequestsResponseDTO);
    }

    @GET
    @Path("/departments")
    @ApiOperation("Lists all available departments")
    @CacheMaxAge(time = 6, unit = TimeUnit.HOURS)
    public Response getAvailableDepartments() {

        final UniversityDepartment[] departments = UniversityDepartment.values();

        final UniversityDepartmentResponseDTO responseDto = universityDepartmentAssembler.toResponse(departments);

        return ResponseUtils.ok(responseDto);
    }


    @DELETE
    @Authenticated
    @Authorized
    @Path("/{userId}")
    @ApiOperation("Deletes a given user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response deleteUser(@PathParam("userId") Integer userId) {
        ServiceResponse<User> response = userService.delete(userId);
        if (response.hasErrors()) {
            return ResponseUtils.anyError(response);
        }
        return ResponseUtils.ok(new ResponseDTO());
    }

    @GET
    @Authenticated
    @Path("/deleted/{userId}")
    @ApiOperation("Returns one specific deleted user claimed by the user id.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getDeletedUserById(@PathParam("userId") Integer userId) {
        ServiceResponse<User> serviceResponse = userService.loadDeletedUser(userId);
        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }
        UserDeletedResponseDTO userDeletedResponseDTO = userAssembler.toDeletedResponse(serviceResponse.getEntity());
        return ResponseUtils.ok(userDeletedResponseDTO);
    }
}