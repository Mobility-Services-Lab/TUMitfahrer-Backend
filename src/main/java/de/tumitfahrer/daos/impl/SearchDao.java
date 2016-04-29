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

import de.tumitfahrer.daos.ISearchDao;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.Search;
import de.tumitfahrer.util.LatLongUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SearchDao extends GenericDao<Search> implements ISearchDao {

    private final static Integer RESULTS_PER_PAGE = 20;

    public SearchDao() {
        super(Search.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ride> find(Search search) {

        // TODO: improve search

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ride.class);

        criteria.add(Restrictions.isNull("deletedAt"));

        if (search.getDeparturePlace() != null && !search.getDeparturePlace().equals("")) {
            criteria.add(Restrictions.like("departurePlace", "%" + search.getDeparturePlace() + "%").ignoreCase());
        }

        if (search.getDestination() != null && !search.getDestination().equals("")) {
            criteria.add(Restrictions.like("destination", "%" + search.getDestination() + "%").ignoreCase());
        }

        if (search.getRideType() != null) {
            criteria.add(Restrictions.eq("rideType", search.getRideType()));
        }

        if (search.getDepartureTime() != null) {
            criteria.add(Restrictions.ge("departureTime", search.getEarliestDepartureTime()));
            criteria.add(Restrictions.le("departureTime", search.getLatestDepartureTime()));
        }

        criteria.addOrder(Order.asc("departureTime"));

        return criteria.list();
    }

    public List<Ride> findGeo(final Search search) {

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ride.class);
        criteria.add(Restrictions.isNull("deletedAt"));

        // lat long departure
        double lowDepLat = LatLongUtil.getLowLat(search.getDepartureLatitude(), search.getDeparturePlaceThreshold());
        double highDepLat = LatLongUtil.getHighLat(search.getDepartureLatitude(), search.getDeparturePlaceThreshold());
        criteria.add(Restrictions.between("departureLatitude", lowDepLat, highDepLat));

        double lowDepLong = LatLongUtil.getLowLong(search.getDepartureLongitude(), search.getDeparturePlaceThreshold());
        double highDepLong = LatLongUtil.getHighLong(search.getDepartureLongitude(), search.getDeparturePlaceThreshold());
        criteria.add(Restrictions.between("departureLongitude", lowDepLong, highDepLong));

        // lat long destination
        double lowDestLat = LatLongUtil.getLowLat(search.getDestinationLatitude(), search.getDestinationThreshold());
        double highDestLat = LatLongUtil.getHighLat(search.getDestinationLatitude(), search.getDestinationThreshold());
        criteria.add(Restrictions.between("destinationLatitude", lowDestLat, highDestLat));

        double lowDestLong = LatLongUtil.getLowLong(search.getDestinationLongitude(), search.getDestinationThreshold());
        double highDestLong = LatLongUtil.getHighLong(search.getDestinationLongitude(), search.getDestinationThreshold());
        criteria.add(Restrictions.between("destinationLongitude", lowDestLong, highDestLong));

        if (search.getRideType() != null) {
            criteria.add(Restrictions.eq("rideType", search.getRideType()));
        }

        if (search.getDepartureTime() != null) {
            criteria.add(Restrictions.ge("departureTime", search.getEarliestDepartureTime()));
            criteria.add(Restrictions.le("departureTime", search.getLatestDepartureTime()));
        }

        criteria.addOrder(Order.asc("departureTime"));

        return criteria.list();
    }

    @Override
    public Integer getSearchesCounter(Integer userID, Date fromDate, Boolean isOwner) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Search.class);
        criteria.add(Restrictions.ge("createdAt", fromDate));

        if (isOwner) {
            criteria.add(Restrictions.eq("userId", userID));
        } else {
            criteria.add(Restrictions.ne("userId", userID));
        }
        return criteria.list().size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Search> getLastSearches() {
        return sessionFactory.getCurrentSession()
                .createCriteria(Search.class)
                .setMaxResults(RESULTS_PER_PAGE)
                .addOrder(Order.desc("createdAt"))
                .list();
    }

}
