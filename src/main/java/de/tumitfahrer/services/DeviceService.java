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

package de.tumitfahrer.services;

import de.tumitfahrer.daos.IDeviceDao;
import de.tumitfahrer.entities.Device;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DeviceService extends AbstractService implements EntityService<Device> {

    private static List<String> VALID_PLATFORMS = Arrays.asList("ios", "android", "windows");
    @Autowired
    private IDeviceDao deviceDao;
    /**
     * Reusing the duration for the api key for the notifications
     */
    @Value("${api.key.validity}")
    private Integer apiKeyValidity;

    public Device load(Integer deviceId) {
        return deviceDao.load(deviceId);
    }


    public ServiceResponse<List<Device>> getDevicesByUserId(Integer userId) {
        ServiceResponse<List<Device>> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(deviceDao.getDevicesByUserId(userId));
        return serviceResponse;
    }

    public ServiceResponse<List<Device>> getEnabledDevicesByUserId(final Integer userId) {
        final ServiceResponse<List<Device>> serviceResponse = ServiceResponseFactory.createInstance();

        serviceResponse.setEntity(deviceDao.getEnabledNonExpiredDevicesByUserId(userId));
        return serviceResponse;
    }

    public ServiceResponse<Device> createOrRenew(Device device) {
        final Device deviceInDatabase = deviceDao.getDeviceByUserId(device.getUserId(), device.getToken());
        if (deviceInDatabase != null) {
            return renewDevice(deviceInDatabase);
        }
        return create(device);
    }

    @Override
    public ServiceResponse<Device> create(Device device) {
        ServiceResponse<Device> serviceResponse = ServiceResponseFactory.createInstance();

        // check inputs
        if (device.getPlatform() == null || !VALID_PLATFORMS.contains(device.getPlatform().toLowerCase())) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("device.invalid_platform"));
        }

        if (device.getToken() == null || device.getToken() == "") {
            serviceResponse.getErrors().add(errorMsgsUtil.get("device.invalid_token"));
        }

        if (device.getLanguage() == null || device.getLanguage() == "") {
            device.setLanguage("en");
        }

        // stop if errors occurred
        if (serviceResponse.hasErrors()) {
            serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);
            return serviceResponse;
        }

        device.setCreatedAt(DateUtils.getCurrentDate());
        device.setUpdatedAt(DateUtils.getCurrentDate());
        device.setDeviceExpires(DateUtils.getCurrentDatePlusOffsetInHours(apiKeyValidity));

        // insert and return
        deviceDao.saveDevice(device);
        serviceResponse.setEntity(device);

        return serviceResponse;
    }

    public ServiceResponse<Device> renewDevice(final Device device) {
        final ServiceResponse<Device> serviceResponse = ServiceResponseFactory.createInstance();
        device.setUpdatedAt(DateUtils.getCurrentDate());
        device.setDeviceExpires(DateUtils.getCurrentDatePlusOffsetInHours(apiKeyValidity));
        device.setEnabled(true);
        deviceDao.update(device);

        serviceResponse.setEntity(device);
        return serviceResponse;
    }

    public boolean isDeviceExpired(final Device device) {
        return device.getDeviceExpires().before(DateUtils.getCurrentDate());
    }

    public ServiceResponse<Device> markDisabled(Integer deviceId) {
        ServiceResponse<Device> serviceResponse = ServiceResponseFactory.createInstance();
        Device device = deviceDao.load(deviceId);
        if (device == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("device.not_found"));
        }
        deviceDao.disableDevice(device);
        serviceResponse.setEntity(device);

        return serviceResponse;
    }

    @Override
    public ServiceResponse<Device> update(Device object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Device> delete(Device object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Device> delete(Integer id) {
        throw new NotImplementedException("TODO");
    }
}