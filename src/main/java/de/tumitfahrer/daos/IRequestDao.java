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

package de.tumitfahrer.daos;

import de.tumitfahrer.entities.Request;
import org.hibernate.criterion.Criterion;

import java.util.Date;
import java.util.List;

public interface IRequestDao extends IGenericDao<Request> {

    List<Request> getRequestsByUserId(Integer userId);

    List<Request> getRequestsByRideId(Integer rideId, Criterion... criterion);

    Request loadByRideAndUserId(Integer rideId, Integer userId);

    Request loadByRideAndUserIdIgnoreAnswered(Integer rideId, Integer userId);

    Integer getRequestsCounter(Integer userID, Date fromDate, Boolean isOwner);

    List<Request> getLastRequests();

    Request loadByRideAndUserIdAnswered(Integer rideId, Integer userId);
}