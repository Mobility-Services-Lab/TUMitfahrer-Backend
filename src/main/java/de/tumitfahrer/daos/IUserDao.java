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

import de.tumitfahrer.entities.User;

public interface IUserDao extends IGenericDao<User> {

    User getUser(String username);

    boolean mailExists(String email);

    User getUserByApiKey(String apiKey);

    Integer getRidesAsDriverCount(Integer userId);

    Integer getRidesAsPassengerCount(Integer userId);

    User loadDeletedUser(Integer userId);
}