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

import de.tumitfahrer.daos.IMessageDao;
import de.tumitfahrer.entities.Message;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageDao extends GenericDao<Message> implements IMessageDao {

    public MessageDao() {
        super(Message.class);
    }

    public List<Message> getByConvId(Integer convId) {
        String sql = "from Message m where m.conversationId = :convId order by m.createdAt asc";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("convId", convId);
        return query.list();
    }

}