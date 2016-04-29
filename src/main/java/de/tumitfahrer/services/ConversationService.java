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

import de.tumitfahrer.daos.IConversationDao;
import de.tumitfahrer.entities.ConvParticipants;
import de.tumitfahrer.entities.Conversation;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConversationService extends AbstractService {

    @Autowired
    private IConversationDao conversationDao;
    @Autowired
    private ConvParticipantsService convParticipantsService;

    public ServiceResponse<Conversation> getByRideId(Integer rideId) {
        ServiceResponse<Conversation> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(conversationDao.getByRideId(rideId));
        return serviceResponse;
    }

    public ServiceResponse<Conversation> getByRideAndConversationId(Integer rideId, Integer conversationId) {
        ServiceResponse<Conversation> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(conversationDao.getByRideAndConversationId(rideId, conversationId));
        return serviceResponse;
    }

    public void create(Ride ride) {
        Conversation conversation = new Conversation();
        conversation.setUserId(ride.getUser().getId());
        conversation.setRide(ride);
        conversation.setCreatedAt(DateUtils.getCurrentDate());
        conversation.setUpdatedAt(DateUtils.getCurrentDate());

        conversationDao.save(conversation);
        conversation = new Conversation();
        conversation = conversationDao.getByRideId(ride.getId());
        ConvParticipants convParticipants = new ConvParticipants();
        convParticipants.setConvId(conversation.getId());
        convParticipants.setUserId(ride.getUser().getId());
        convParticipants.setCreatedAt(DateUtils.getCurrentDate());
        convParticipants.setUpdatedAt(DateUtils.getCurrentDate());
        convParticipantsService.create(convParticipants);
    }

    public ServiceResponse<Conversation> delete(Ride ride) {
        Conversation conversation = getByRideId(ride.getId()).getEntity();
        //convParticipantsService.delete(conversation.getId());
        conversation.setDeletedAt(DateUtils.getCurrentDate());
        conversationDao.update(conversation);
        return null;
    }

    public void update(Integer convId, Date updatedDate) {
        Conversation conversation = conversationDao.load(convId);
        conversation.setUpdatedAt(updatedDate);

        conversationDao.update(conversation);
    }
}
