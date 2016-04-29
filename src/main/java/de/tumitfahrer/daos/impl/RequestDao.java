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

import de.tumitfahrer.daos.IRequestDao;
import de.tumitfahrer.entities.Request;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RequestDao extends GenericDao<Request> implements IRequestDao {

    private final static Integer RESULTS_PER_PAGE = 5;

    public RequestDao() {
        super(Request.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request loadByRideAndUserId(Integer rideId, Integer userId) {
        List<Request> requests = sessionFactory.getCurrentSession().createCriteria(Request.class)
                .add(Restrictions.eq("rideId", rideId))
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.isNull("answeredAt"))
                .list();

        if (requests.isEmpty()) {
            return null;
        }

        return requests.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request loadByRideAndUserIdIgnoreAnswered(Integer rideId, Integer userId) {
        List<Request> requests = sessionFactory.getCurrentSession().createCriteria(Request.class)
                .add(Restrictions.eq("rideId", rideId))
                .add(Restrictions.eq("userId", userId))
                .list();

        if (requests.isEmpty()) {
            return null;
        }

        return requests.get(0);
    }

    @SuppressWarnings("unchecked")
    public Request loadByRideAndUserIdAnswered(Integer rideId, Integer userId) {
        List<Request> requests = sessionFactory.getCurrentSession().createCriteria(Request.class)
                .add(Restrictions.eq("rideId", rideId))
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.isNotNull("answeredAt"))
                .list();

        if (requests.isEmpty()) {
            return null;
        }

        return requests.get(0);
    }

    public List<Request> getRequestsByUserId(Integer userId) {
        return findByCriterion(
                Restrictions.eq("userId", userId),
                Restrictions.isNull("answeredAt")
        );
    }

    @Override
    public List<Request> getRequestsByRideId(Integer rideId, Criterion... criterions) {
        List<Criterion> criteriaList = new ArrayList<>();
        criteriaList.add(Restrictions.eq("rideId", rideId));
        criteriaList.add(Restrictions.isNull("answeredAt"));
        Collections.addAll(criteriaList, criterions);
        return findByCriterion(criteriaList);
    }

    @Override
    public Integer getRequestsCounter(Integer userID, Date fromDate, Boolean isOwner) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Request.class);
        criteria.add(Restrictions.ge("createdAt", fromDate));

        if (isOwner != null && isOwner) {
            criteria.add(Restrictions.eq("userId", userID));
        } else {
            criteria.add(Restrictions.ne("userId", userID));
        }
        return criteria.list().size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Request> getLastRequests() {
        return sessionFactory.getCurrentSession().createCriteria(Request.class)
                .add(Restrictions.isNull("answeredAt"))
                .setMaxResults(RESULTS_PER_PAGE)
                .addOrder(Order.desc("createdAt"))
                .list();
    }

}