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

import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.util.DateUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDao extends GenericDao<User> implements IUserDao {

    @Autowired
    private SessionFactory sessionFactory;

    public UserDao() {
        super(User.class);
    }

    @Override
    public User load(Integer userId) {
        User user = super.load(userId);
        if (user != null && !user.isDeleted()) {
            return user;
        }
        return null;
    }

    @Override
    public User getUser(String email) {
        String sql = "from User where email = :email and deleted_at = null";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("email", email);

        List users = query.list();
        if (users.isEmpty()) {
            return null;
        }

        return (User) users.get(0);
    }

    @Override
    public boolean mailExists(String email) {
        String sql = "from User where email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("email", email);

        return !query.list().isEmpty();
    }

    @Override
    public User getUserByApiKey(String apiKey) {
        String sql = "from User where apiKey = :apiKey";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("apiKey", apiKey);

        List users = query.list();
        if (users.isEmpty()) {
            return null;
        }

        return (User) users.get(0);
    }

    @Override
    public Integer getRidesAsDriverCount(Integer userId) {
        String sql = "select * from relationships where user_id = :userId and is_driving = true";
        return sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .setParameter("userId", userId)
                .list()
                .size();
    }

    @Override
    public Integer getRidesAsPassengerCount(Integer userId) {
        String sql = "select * from relationships where user_id = :userId and is_driving = false";
        return sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .setParameter("userId", userId)
                .list()
                .size();
    }


    @Override
    public void delete(User user) {
        user.setDeletedAt(DateUtils.getCurrentDate());
        user.setApiKey(null);
        user.setApiKeyExpires(null);
        update(user);
    }


    @Override
    public User loadDeletedUser(Integer userId) {
        User user = super.load(userId);
        if (user != null && user.isDeleted()) {
            return user;
        }
        return null;
    }
}