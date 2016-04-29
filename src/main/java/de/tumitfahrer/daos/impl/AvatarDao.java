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

import de.tumitfahrer.daos.IAvatarDao;
import de.tumitfahrer.entities.Avatar;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AvatarDao extends GenericDao<Avatar> implements IAvatarDao {

    @Autowired
    private SessionFactory sessionFactory;

    public AvatarDao() {
        super(Avatar.class);
    }

    @Override
    public void saveAvatar(Avatar avatar) {
        sessionFactory.getCurrentSession().save(avatar);
    }

    @Override
    public Avatar getAvatar(Integer id) {
        String sql = "from Avatar where id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("id", id);

        List avatars = query.list();
        if (avatars.isEmpty()) {
            return null;
        }

        return (Avatar) avatars.get(0);
    }
}