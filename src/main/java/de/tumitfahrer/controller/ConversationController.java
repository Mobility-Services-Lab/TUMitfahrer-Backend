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
import de.tumitfahrer.assembler.ConversationAssembler;
import de.tumitfahrer.entities.Conversation;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.ConversationService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/rides/{rideId}/conversations")
@Api(value = "Conversation Controller", description = "All conversations are managed here.")
public class ConversationController extends AbstractController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ConversationAssembler conversationAssembler;

    @GET
    @Authenticated
    @Authorized
    @ApiOperation("Lists all conversations for a specific ride.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getConversations(@PathParam("rideId") Integer rideId) {
        ServiceResponse<Conversation> serviceResponse = conversationService.getByRideId(rideId);
        return ResponseUtils.ok(conversationAssembler.toResponse(serviceResponse.getEntity()));
    }

    @GET
    @Authenticated
    @Authorized
    @Path("/{conversationId}")
    @ApiOperation("Returns a specific conversation.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getConversation(@PathParam("rideId") Integer rideId, @PathParam("conversationId") Integer conversationId) {
        ServiceResponse<Conversation> serviceResponse = conversationService.getByRideAndConversationId(rideId, conversationId);
        return ResponseUtils.ok(conversationAssembler.toResponse(serviceResponse.getEntity()));
    }
}