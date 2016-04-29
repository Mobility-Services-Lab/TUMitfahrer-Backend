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

import de.tumitfahrer.daos.IRatingDao;
import de.tumitfahrer.entities.Rating;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RatingDao extends GenericDao<Rating> implements IRatingDao {

    @Autowired
    private SessionFactory sessionFactory;

    public RatingDao() {
        super(Rating.class);
    }

    @Override
    public void saveRating(Rating rating) {
        sessionFactory.getCurrentSession().save(rating);
    }

    @Override
    public List<Rating> getRatings() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Rating.class);
        return criteria.list();
    }

    @Override
    public List<Rating> getRatingsByUserId(Integer userId) {
        String sql = "from Rating where toUserId = :userId or fromUserId = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public List<Rating> getRatingsFromUserId(Integer userId) {
        String sql = "from Rating where fromUserId = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public List<Rating> getRatingsToUserId(Integer userId) {
        String sql = "from Rating where toUserId = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public List<Rating> getByRideId(Integer rideId) {
        String sql = "from Rating where rideId = :rideId";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("rideId", rideId);
        return query.list();
    }
}