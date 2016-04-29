package de.tumitfahrer.services;


import de.tumitfahrer.daos.IConvParticipantsDao;
import de.tumitfahrer.entities.ConvParticipants;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvParticipantsService implements EntityService<ConvParticipants> {

    @Autowired
    ConversationService conversationService;
    @Autowired
    private IConvParticipantsDao convparticipantsDao;

    @Override
    public ServiceResponse<ConvParticipants> create(ConvParticipants conv_participants) {
        ServiceResponse<ConvParticipants> serviceResponse = ServiceResponseFactory.createInstance();

        if (conv_participants.getUserId() == null || conv_participants.getUserId() < 0) {
            serviceResponse.getErrors().add("invalid user id");
        }
        if (conv_participants.getConvId() == null || conv_participants.getConvId() < 0) {
            serviceResponse.getErrors().add("invalid conversation id");
        }

        // stop if errors occurred
        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        // insert and return
        convparticipantsDao.save(conv_participants);
        serviceResponse.setEntity(conv_participants);

        return serviceResponse;
    }


    public ConvParticipants getByConvAndUserId(Integer convId, Integer userId) {
        return convparticipantsDao.getByConvAndUserId(convId, userId);
    }

    public ServiceResponse<ConvParticipants> addParticipant(Passenger passenger) {
        ServiceResponse<ConvParticipants> serviceResponse = ServiceResponseFactory.createInstance();

        ConvParticipants convParticipants = new ConvParticipants();

        convParticipants.setConvId(conversationService.getByRideId(passenger.getRideId()).getEntity().getId());
        convParticipants.setUserId(passenger.getUserId());
        convParticipants.setCreatedAt(DateUtils.getCurrentDate());
        convParticipants.setUpdatedAt(DateUtils.getCurrentDate());
        convparticipantsDao.save(convParticipants);
        serviceResponse.setEntity(convParticipants);
        return serviceResponse;
    }


    public ServiceResponse<ConvParticipants> removeParticipant(Passenger passenger) {
        ConvParticipants convParticipants = new ConvParticipants();
        convParticipants = getByConvAndUserId(conversationService.getByRideId(passenger.getRideId()).getEntity().getId(), passenger.getUserId());
        convparticipantsDao.delete(convParticipants);
        return ServiceResponseFactory.createInstance();
    }

    @Override
    public ServiceResponse<ConvParticipants> update(ConvParticipants object) {
        return null;
    }

    @Override
    public ServiceResponse<ConvParticipants> delete(ConvParticipants participants) {
        if (participants != null) {

            convparticipantsDao.delete(participants);
        }
        return ServiceResponseFactory.createInstance();
    }

    @Override
    public ServiceResponse<ConvParticipants> delete(Integer convId) {
        List<ConvParticipants> participants = convparticipantsDao.getByConvId(convId);
        while (participants.size() != 0) {
            convparticipantsDao.delete(participants.get(0));
            participants.remove(0);
        }
        return null;
    }


}
