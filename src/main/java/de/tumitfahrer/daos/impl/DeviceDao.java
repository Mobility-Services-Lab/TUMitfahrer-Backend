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

import de.tumitfahrer.daos.IDeviceDao;
import de.tumitfahrer.entities.Device;
import de.tumitfahrer.util.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeviceDao extends GenericDao<Device> implements IDeviceDao {

    @Autowired
    private SessionFactory sessionFactory;

    public DeviceDao() {
        super(Device.class);
    }

    @Override
    public void saveDevice(Device device) {
        sessionFactory.getCurrentSession().save(device);
    }

    @Override
    public List<Device> getDevicesByUserId(Integer userId) {
        String sql = "from Device where userId = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public Device getDeviceByUserId(final Integer userId, final String token) {
        final Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Device.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("token", token))
                .setMaxResults(1);

        final List<Device> devices = criteria.list();

        return devices.size() > 0 ? devices.get(0) : null;
    }

    @Override
    public List<Device> getEnabledDevicesByUserId(Integer userId) {
        String sql = "from Device where userId = :userId and enabled = true";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setParameter("userId", userId);
        return query.list();
    }

    @Override
    public List<Device> getEnabledNonExpiredDevicesByUserId(Integer userId) {
        final Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Device.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("enabled", true))
                .add(Restrictions.ge("deviceExpires", DateUtils.getCurrentDate()));

        return criteria.list();
    }

    @Override
    public List<Device> getDevices() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Device.class);
        return criteria.list();
    }

    @Override
    public void disableDevice(Device device) {
        device.setEnabled(false);
        device.setUpdatedAt(DateUtils.getCurrentDate());
        update(device);
    }
}