package de.tumitfahrer.daos.impl;


import de.tumitfahrer.daos.IConvParticipantsDao;
import de.tumitfahrer.entities.ConvParticipants;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConvParticipantsDao extends GenericDao<ConvParticipants> implements IConvParticipantsDao {

    public ConvParticipantsDao() {
        super(ConvParticipants.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ConvParticipants getByConvAndUserId(Integer convId, Integer userId) {
        List<ConvParticipants> participants = sessionFactory.getCurrentSession().createCriteria(ConvParticipants.class)
                .add(Restrictions.eq("convId", convId))
                .add(Restrictions.eq("userId", userId))
                .list();

        if (participants.isEmpty()) {
            return null;
        }

        return participants.get(0);


    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConvParticipants> getByConvId(Integer convId) {
        List<ConvParticipants> participants = sessionFactory.getCurrentSession().createCriteria(ConvParticipants.class)
                .add(Restrictions.eq("convId", convId))
                .list();

        return participants;


    }


}
