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
import de.tumitfahrer.dtos.session.SessionResponseDTO;
import de.tumitfahrer.dtos.user.request.PasswordRequestDTO;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.PasswordService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/password")
@Api(value = "Password Controller", description = "Used to reset the password of a user.")
public class PasswordController extends AbstractController {

    @Autowired
    private PasswordService passwordService;

    @POST
    @Path("/forgot")
    @ApiOperation("Resets the password of a user identified by the email address.")
    public Response resetPassword(@QueryParam("email") String email) {
        ServiceResponse<User> serviceResponse = passwordService.resetPassword(email);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        return ResponseUtils.ok();
    }

    @PUT
    @ApiOperation("Changes the password of a user.")
    public Response changePassword(@HeaderParam("Authorization") String basicAuth, PasswordRequestDTO passwordRequestDTO) {
        ServiceResponse<User> serviceResponse = passwordService.changePassword(basicAuth, passwordRequestDTO);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        SessionResponseDTO sessionResponseDTO = SessionResponseDTO.createInstance();
        mapper.map(serviceResponse.getEntity(), sessionResponseDTO.getUser());

        return ResponseUtils.ok(sessionResponseDTO);
    }
}