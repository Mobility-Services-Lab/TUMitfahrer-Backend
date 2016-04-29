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

package de.tumitfahrer.services;

import de.tumitfahrer.daos.IMessageDao;
import de.tumitfahrer.entities.Message;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MessageService extends AbstractService implements EntityService<Message> {

    @Autowired
    private IMessageDao messageDao;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private NotificationService notificationService;


    public ServiceResponse<Message> create(Integer rideId, final Message message) {
        final Set<ConstraintViolation<Message>> violations = validator.validate(message);
        if (violations.size() > 0) {
            return ServiceResponseFactory.fromViolations(violations);
        }

        message.setCreatedAt(DateUtils.getCurrentDate());
        message.setUpdatedAt(DateUtils.getCurrentDate());

        // insert and return
        messageDao.save(message);

        //update conversation updatedtime
        conversationService.update(message.getConversationId(), message.getCreatedAt());
        notificationService.notifyNewMessage(rideId);
        final ServiceResponse<Message> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(message);

        return serviceResponse;
    }

    @Override
    public ServiceResponse<Message> create(Message object) {
        throw new NotImplementedException("TODO");
    }


    @Override
    public ServiceResponse<Message> update(Message object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Message> delete(Message object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Message> delete(Integer id) {
        throw new NotImplementedException("TODO");
    }

    public ServiceResponse<List<Message>> getByConvId(Integer convId) {
        ServiceResponse<List<Message>> serviceResponse = ServiceResponseFactory.createInstance();

        List<Message> messages = new ArrayList<>();
        messages = messageDao.getByConvId(convId);
        serviceResponse.setEntity(messages);
        return serviceResponse;
    }


}