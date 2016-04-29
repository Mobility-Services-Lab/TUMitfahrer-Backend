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

import de.tumitfahrer.daos.IRideDao;
import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.util.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RideDao extends GenericDao<Ride> implements IRideDao {

    private final static Integer RESULTS_PER_PAGE = 20;

    public RideDao() {
        super(Ride.class);
    }

    @Override
    public Ride load(Integer rideId) {
        Ride ride = super.load(rideId);
        if (ride != null && !ride.isDeleted()) {
            return ride;
        }
        return null;
    }

    @Override
    public void delete(Ride ride) {
        ride.setDeletedAt(DateUtils.getCurrentDate());
        update(ride);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getAllRides(Integer page, Date fromDate) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Ride.class)
                .add(Restrictions.ge("departureTime", fromDate))
                .add(Restrictions.isNull("deletedAt"))
                .setFirstResult(page * RESULTS_PER_PAGE)
                .setMaxResults(RESULTS_PER_PAGE)
                .addOrder(Order.asc("departureTime"));

        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getAllRides(Integer page, Integer rideType, Date fromDate) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Ride.class)
                .add(Restrictions.eq("rideType", rideType))
                .add(Restrictions.ge("departureTime", fromDate))
                .add(Restrictions.isNull("deletedAt"))
                .setFirstResult(page * RESULTS_PER_PAGE)
                .setMaxResults(RESULTS_PER_PAGE)
                .addOrder(Order.asc("departureTime"));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> getAllRideIds() {
        return sessionFactory.getCurrentSession()
                .createQuery("select r.id from Ride r where r.deletedAt is null order by r.id asc")
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getRidesByUserId(Integer userId) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Ride.class)
                .add(Restrictions.eq("user.id", userId))
                .add(Restrictions.isNull("deletedAt"))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getRidesByUserId(Integer userId, boolean driver, boolean passenger, boolean past, boolean future) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Ride.class, "ride");

        criteria.createAlias("ride.passengers", "passenger");
        criteria.add(Restrictions.eq("passenger.userId", userId));
        criteria.add(Restrictions.isNull("deletedAt"));

        if (driver) {
            criteria.add(Restrictions.eq("passenger.isDriver", true));
        } else if (passenger) {
            criteria.add(Restrictions.eq("passenger.isDriver", false));
        }

        if (past) {
            criteria.add(Restrictions.lt("departureTime", DateUtils.getCurrentDate()));
        } else if (future) {
            criteria.add(Restrictions.ge("departureTime", DateUtils.getCurrentDate()));
        }

        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getRidesByOwnerAndDate(Integer userID, Integer rideType, Date fromDate, Boolean isOwner) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Ride.class, "ride")
                .add(Restrictions.eq("rideType", rideType))
                .add(Restrictions.ge("createdAt", fromDate))
                .add(Restrictions.isNull("deletedAt"));

        if (isOwner) {
            criteria.add(Restrictions.eq("user.id", userID));
        } else {
            criteria.add(Restrictions.ne("user.id", userID));
        }
        criteria.addOrder(Order.desc("id")); //TODO unique results ?? why there are multiple resultsets+
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> getRidesByDeparture(Date fromDate) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Ride.class)
                .add(Restrictions.gt("departureTime", fromDate))
                .add(Restrictions.isNull("deletedAt"))
                .addOrder(Order.desc("departureTime"))
                .setMaxResults(RESULTS_PER_PAGE);
        return criteria.list();
    }

    @Override
    public Boolean isRideRequest(Integer rideId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ride.class, "ride");

        criteria.createAlias("ride.passengers", "passenger");

        criteria.add(Restrictions.eq("id", rideId));
        criteria.add(Restrictions.isNull("deletedAt"));
        criteria.add(Restrictions.eqProperty("passenger.userId", "ride.user.id"));

        criteria.add(Restrictions.eq("passenger.isDriver", false));

        return !criteria.list().isEmpty();
    }

    @Override
    public Integer getPassengerCount(Integer rideId) {
        String sql = "select * from relationships where ride_id = :rideId and is_driving = false";
        return sessionFactory.getCurrentSession()
                .createSQLQuery(sql)
                .setParameter("rideId", rideId)
                .list()
                .size();
    }

    public Integer newPassengerCount(Integer userId, Date fromDate) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Passenger.class);
        criteria.add(Restrictions.ge("createdAt", fromDate));
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("isDriver", false));
        return criteria.list().size();
    }

    @Override
    public Ride loadDeletedRide(Integer rideId) {
        Ride ride = super.load(rideId);
        if (ride != null && ride.isDeleted()) {
            return ride;
        }
        return null;
    }
}