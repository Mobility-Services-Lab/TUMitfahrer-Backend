package de.tumitfahrer.assembler;


import de.tumitfahrer.dtos.message.MessageDTO;
import de.tumitfahrer.dtos.message.MessageResponseDTO;
import de.tumitfahrer.entities.Message;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageAssembler extends AbstractAssembler {

    @Autowired
    private UserService userService;

    public List<MessageDTO> toDto(List<Message> messages) {
        List<MessageDTO> messagesDTO = new ArrayList<>();
        MessageDTO messageDTO;

        for (Message message : messages) {
            messageDTO = toDto(message);
            messagesDTO.add(messageDTO);
        }

        return messagesDTO;
    }

    public MessageDTO toDto(Message message) {
        MessageDTO messageDTO = mapper.map(message, MessageDTO.class);
        messageDTO.setContent(message.getContent());
        messageDTO.setDateSent(message.getCreatedAt().toString());
        User user = userService.load(message.getSenderId());
        messageDTO.setSenderName(user.getFirstName() + " " + user.getLastName());

        return messageDTO;
    }

    public MessageResponseDTO toResponse(MessageDTO messageDTO) {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
        messageResponseDTO.setMessage(messageDTO);
        return messageResponseDTO;
    }

    public MessageResponseDTO toResponse(Message message) {
        return toResponse(toDto(message));
    }
}
