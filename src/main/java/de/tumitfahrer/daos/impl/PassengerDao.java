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

import de.tumitfahrer.daos.IPassengerDao;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.util.DateUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PassengerDao extends GenericDao<Passenger> implements IPassengerDao {

    public PassengerDao() {
        super(Passenger.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Passenger loadByRideAndUserId(Integer rideId, Integer userId) {
        List<Passenger> passengers = sessionFactory.getCurrentSession().createCriteria(Passenger.class)
                .add(Restrictions.eq("rideId", rideId))
                .add(Restrictions.eq("userId", userId))
                .list();

        if (passengers.isEmpty()) {
            return null;
        }

        return passengers.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getByRideId(Integer rideId) {
        String sql = "from User u where u.id in (select p.userId from Passenger p where p.rideId = :rideId)";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("rideId", rideId);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getByUserId(Integer userId) {
        String sql = "from Ride r where r.id in (select p.rideId from Passenger p where p.userId = :userId)";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public List<Ride> getFutureRidesByUserId(Integer userId) {
        String sql = "from Ride r where r.id in (select p.rideId from Passenger p where p.userId = :userId) and r.departureTime >= :currentDate";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("currentDate", DateUtils.getCurrentDate());
        return query.list();
    }
}