package de.tumitfahrer.daos;

import de.tumitfahrer.entities.ConvParticipants;

import java.util.List;

public interface IConvParticipantsDao extends IGenericDao<ConvParticipants> {

    ConvParticipants getByConvAndUserId(Integer convId, Integer userId);

    List<ConvParticipants> getByConvId(Integer convId);
}
