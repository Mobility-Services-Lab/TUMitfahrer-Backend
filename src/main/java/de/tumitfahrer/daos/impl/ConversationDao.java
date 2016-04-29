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

package de.tumitfahrer.daos.impl;

import de.tumitfahrer.daos.IConversationDao;
import de.tumitfahrer.entities.Conversation;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConversationDao extends GenericDao<Conversation> implements IConversationDao {

    @Autowired
    private SessionFactory sessionFactory;

    public ConversationDao() {
        super(Conversation.class);
    }

    @Override
    public Conversation getByRideId(Integer rideId) {
        String sql = "from Conversation where ride.id = :rideId and deletedAt = null";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("rideId", rideId);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        }

        return (Conversation) result.get(0);
    }

    @Override
    public Conversation getByRideAndConversationId(Integer rideId, Integer conversationId) {
        String sql = "from Conversation where ride.id = :rideId and id = :conversationId and deletedAt = null";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("rideId", rideId);
        query.setParameter("conversationId", conversationId);

        List result = query.list();
        if (result.isEmpty()) {
            return null;
        }

        return (Conversation) result.get(0);
    }


}