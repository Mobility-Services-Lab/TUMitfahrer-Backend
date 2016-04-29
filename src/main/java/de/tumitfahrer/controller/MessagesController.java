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
import de.tumitfahrer.assembler.MessageAssembler;
import de.tumitfahrer.dtos.message.MessageRequestDTO;
import de.tumitfahrer.dtos.message.MessageResponseDTO;
import de.tumitfahrer.entities.Message;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.MessageService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/rides/{rideId}/conversations/{conversationId}/messages")
@Api(value = "Messages Controller", description = "Manages all messages between ride conversations.")
public class MessagesController extends AbstractController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageAssembler messageAssembler;

    @POST
    @Authenticated
    @Authorized
    @ApiOperation("Creates a message in a conversation.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response createMessage(@PathParam("rideId") Integer rideId, @PathParam("conversationId") Integer conversationId, MessageRequestDTO messageRequestDTO) {
        Message message = mapper.map(messageRequestDTO, Message.class);
        message.setConversationId(conversationId);

        ServiceResponse<Message> serviceResponse = messageService.create(rideId, message);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }
        MessageResponseDTO messageResponseDTO = messageAssembler.toResponse(serviceResponse.getEntity());
//		MessageResponseDTO messageResponseDTO = mapper.map(message, MessageResponseDTO.class);
        return ResponseUtils.ok(messageResponseDTO);
    }
}