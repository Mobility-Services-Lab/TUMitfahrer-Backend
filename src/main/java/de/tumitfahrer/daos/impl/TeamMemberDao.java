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

import de.tumitfahrer.daos.ITeamMemberDao;
import de.tumitfahrer.entities.TeamMember;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeamMemberDao extends GenericDao<TeamMember> implements ITeamMemberDao {

    @Autowired
    private SessionFactory sessionFactory;

    public TeamMemberDao() {
        super(TeamMember.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TeamMember> getByTeam(String team) {
        return sessionFactory.getCurrentSession()
                .createCriteria(TeamMember.class)
                .add(Restrictions.eq("team", team))
                .addOrder(Order.asc("id"))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TeamMember> getAll() {
        return sessionFactory.getCurrentSession()
                .createCriteria(TeamMember.class)
                .addOrder(Order.asc("id"))
                .list();
    }
}