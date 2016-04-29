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
import de.tumitfahrer.dtos.session.SessionResponseDTO;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.services.SessionService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/sessions")
@Api(value = "Session Controller", description = "Api key management.")
public class SessionsController extends AbstractController {

    @Autowired
    private SessionService sessionService;

    @POST
    @ApiOperation(value = "Claims personal api key.", notes = "The api key will be reset and will be invalidated after 12h idle time!")
    public Response generateApiKey(@HeaderParam("Authorization") String basicAuth) {
        ServiceResponse<User> serviceResponse = sessionService.createSession(basicAuth);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        SessionResponseDTO sessionResponseDTO = SessionResponseDTO.createInstance();
        mapper.map(serviceResponse.getEntity(), sessionResponseDTO.getUser());

        return ResponseUtils.ok(sessionResponseDTO);
    }

    @PUT
    @Authenticated
    @ApiOperation(value = "Checks validity of an api key.", notes = "Unauthorized if not valid. OK json if valid.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response checkApiKey() {
        return ResponseUtils.ok();
    }

    @DELETE
    @Authenticated
    @ApiOperation(value = "Destroys an api key", notes = "The api key will be destroyed.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response destroyApiKey(@HeaderParam("Authorization") String apiKey) {
        ServiceResponse<User> serviceResponse = sessionService.destroySession(apiKey);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }
        return ResponseUtils.ok();
    }
}
