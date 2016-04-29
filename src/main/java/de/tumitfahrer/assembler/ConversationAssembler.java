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

package de.tumitfahrer.assembler;

import de.tumitfahrer.dtos.conversation.response.ConversationDTO;
import de.tumitfahrer.dtos.conversation.response.ConversationResponseDTO;
import de.tumitfahrer.dtos.conversation.response.ConversationsResponseDTO;
import de.tumitfahrer.entities.Conversation;
import de.tumitfahrer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationAssembler extends AbstractResponseAssembler<Conversation, ConversationDTO, ConversationResponseDTO, ConversationsResponseDTO> {

    @Autowired
    MessageService messageService;
    @Autowired
    MessageAssembler messageAssembler;
    @Autowired
    RideAssembler rideAssembler;

    ConversationAssembler() {
        super(Conversation.class, ConversationDTO.class, ConversationResponseDTO.class, ConversationsResponseDTO.class);
    }

    public ConversationDTO toDto(Conversation conversation) {
        ConversationDTO conversationDTO = mapper.map(conversation, ConversationDTO.class);
        conversationDTO.setRide(rideAssembler.toDto(conversation.getRide()));
        conversationDTO.setUserId(conversation.getUserId());
        conversationDTO.setMessages(messageAssembler.toDto(messageService.getByConvId(conversation.getId()).getEntity()));
        return conversationDTO;
    }


    @Override
    public ConversationResponseDTO toResponse(ConversationDTO conversationDTO) {
        ConversationResponseDTO conversationResponseDTO = new ConversationResponseDTO();
        conversationResponseDTO.setConversation(conversationDTO);
        return conversationResponseDTO;
    }

    @Override
    public ConversationsResponseDTO toResponseFromDto(List<ConversationDTO> conversationsDTO) {
        ConversationsResponseDTO conversationsResponseDTO = new ConversationsResponseDTO();
        conversationsResponseDTO.setConversations(conversationsDTO);
        return conversationsResponseDTO;
    }

    public ConversationResponseDTO toResponse(Conversation conversation) {
        return toResponse(toDto(conversation));
    }
}
