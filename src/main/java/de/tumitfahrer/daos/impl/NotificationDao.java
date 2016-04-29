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

import de.tumitfahrer.daos.INotificationDao;
import de.tumitfahrer.entities.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationDao extends GenericDao<Notification> implements INotificationDao {

    public NotificationDao() {
        super(Notification.class);
    }

    @Override
    public void saveNotification(Notification notification) {
        sessionFactory.getCurrentSession().save(notification);
    }
}
