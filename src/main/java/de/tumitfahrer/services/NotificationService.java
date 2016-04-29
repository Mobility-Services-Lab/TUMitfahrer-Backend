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

import de.tumitfahrer.daos.INotificationDao;
import de.tumitfahrer.daos.IRideDao;
import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.daos.impl.DeviceDao;
import de.tumitfahrer.entities.Device;
import de.tumitfahrer.entities.Notification;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.enums.Language;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.LanguageUtils;
import de.tumitfahrer.util.notification.GcmResponse;
import de.tumitfahrer.util.notification.GcmServerSideSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService extends AbstractService implements EntityService<Notification> {

    @Autowired
    private INotificationDao notificationDao;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private RideService rideService;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private IRideDao rideDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private LanguageUtils languageUtils;
    @Autowired
    private GcmServerSideSender gcm;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private ConvParticipantsService convParticipantsService;

    @Override
    public ServiceResponse<Notification> create(Notification notification) {
        ServiceResponse<Notification> serviceResponse = ServiceResponseFactory.createInstance();

        // check inputs
        if (notification.getUserId() == null || notification.getUserId() <= 0) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("passenger.invalid_id"));
        }

        if (notification.getRideId() == null || notification.getRideId() <= 0) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("ride.invalid_id"));
        }

        // stop if errors occurred
        if (serviceResponse.hasErrors()) {
            return serviceResponse;
        }

        notification.setCreatedAt(DateUtils.getCurrentDate());
        notification.setUpdatedAt(DateUtils.getCurrentDate());
        notification.setStatus(Notification.NotificationStatus.NOTSEND);

        // insert and return
        notificationDao.saveNotification(notification);
        serviceResponse.setEntity(notification);

        return serviceResponse;
    }

    public boolean notifyRequestRide(Integer requesterUserId, Integer rideId) {
        Notification notification = new Notification();
        int results = 0;

        Ride ride = rideService.load(rideId);
        if (ride == null) {
            return false;
        }
        User receipient = ride.getUser();
        if (receipient == null) {
            return false;
        }

        //Create and save notification to DB
        notification.setUserId(receipient.getId());
        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.REQUESTRIDE);
        notification.setCreator(requesterUserId);
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        if (this.create(notification).hasErrors()) {
            //TODO error handling
        }

        //Find available devices and send notifications to them
        List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
        for (Device device : userDevices) {
            if (sendNotification(device, notification)) {
                results++;
            }
        }

        return results == userDevices.size();

    }


    public boolean notifyPassengerAdded(Integer passengerUserId, Integer rideId) {
        Notification notification = new Notification();
        int results = 0;

        Ride ride = rideService.load(rideId);
        if (ride == null) {
            return false;
        }
        User receipient = userService.load(passengerUserId);
        if (receipient == null) {
            return false;
        }
        User creator = ride.getUser();
        if (creator == null) {
            return false;
        }

        notification.setUserId(receipient.getId());
        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.PASSENGERACCEPTED);
        notification.setCreator(creator.getId());
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        if (this.create(notification).hasErrors()) {
            //TODO error handling
        }

        //Find available devices and send notifications to them
        List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
        for (Device device : userDevices) {
            if (sendNotification(device, notification)) {
                results++;
            }
        }

        return results == userDevices.size();
    }

    public boolean notifyRequestDeclined(Integer requesterUserId, Integer rideId) {
        Notification notification = new Notification();
        int results = 0;

        Ride ride = rideService.load(rideId);
        if (ride == null) {
            return false;
        }
        User receipient = userService.load(requesterUserId);
        if (receipient == null) {
            return false;
        }
        User creator = ride.getUser();
        if (creator == null) {
            return false;
        }

        notification.setUserId(receipient.getId());
        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.REQUESTRIDEDECLINED);
        notification.setCreator(creator.getId());
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        if (this.create(notification).hasErrors()) {
            //TODO error handling
        }

        //Find available devices and send notifications to them
        List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
        for (Device device : userDevices) {
            if (sendNotification(device, notification)) {
                results++;
            }
        }

        return results == userDevices.size();
    }

    public boolean notifyPassengerRemoved(Integer passengerUserId, Integer rideId) {
        Notification notification = new Notification();
        int results = 0;

        Ride ride = rideService.load(rideId);
        if (ride == null) {
            return false;
        }

        User receipient = userService.load(passengerUserId);
        if (receipient == null) {
            return false;
        }

        User creator = ride.getUser();
        if (creator == null) {
            return false;
        }

        notification.setUserId(receipient.getId());
        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.PASSENGERREMOVED);
        notification.setCreator(creator.getId());
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        if (this.create(notification).hasErrors()) {
            //TODO error handling
        }

        //Find available devices and send notifications to them
        List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
        for (Device device : userDevices) {
            if (sendNotification(device, notification)) {
                results++;
            }
        }

        return results == userDevices.size();
    }

    public boolean notifyPassengerLeft(Integer passengerUserId, Integer rideId) { //A passenger willingly leaves a ride
        Notification notification = new Notification();
        int results = 0;

        Ride ride = rideService.load(rideId);
        if (ride == null) {
            return false;
        }

        User receipient = ride.getUser();
        if (receipient == null) {
            return false;
        }

        //Create and save notification to DB
        notification.setUserId(receipient.getId());
        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.PASSENGERLEFT);
        notification.setCreator(passengerUserId);
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        if (this.create(notification).hasErrors()) {
            //TODO error handling
        }

        //Find available devices and send notifications to them
        List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
        for (Device device : userDevices) {
            if (sendNotification(device, notification)) {
                results++;
            }
        }

        return results == userDevices.size();
    }

    public boolean notifyOfferedRide(Integer requesterId, Integer rideId) {

        Notification notification = new Notification();
        int results = 0;

        Ride ride = rideService.load(rideId);
        if (ride == null) {
            return false;
        }
        User receipient = userService.load(requesterId);
        if (receipient == null) {
            return false;
        }
        User creator = ride.getUser();
        if (creator == null) {
            return false;
        }

        notification.setUserId(receipient.getId());
        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.RIDEOFFERED);
        notification.setCreator(creator.getId());
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        if (this.create(notification).hasErrors()) {
            //TODO error handling
        }

        //Find available devices and send notifications to them
        List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
        for (Device device : userDevices) {
            if (sendNotification(device, notification)) {
                results++;
            }
        }

        return results == userDevices.size();

    }

    public void notifyRideCanceled(Integer rideId) {
        Notification notification = new Notification();


        Ride ride = rideService.load(rideId);
        if (ride == null) { // FIXME konstantinos
            return;
        }
        User creator = ride.getUser();
        if (creator == null) {
            return;
        }

        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.RIDECANCELED);
        notification.setCreator(creator.getId());
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        //Find all passengers and send them notification
        List<User> passengers = passengerService.getByRideId(rideId).getEntity();
        for (User receipient : passengers) {

            if (receipient.getId() == ride.getUser().getId()) {       //Do not send to the owner
                continue;
            }

            notification.setUserId(receipient.getId());
            if (this.create(notification).hasErrors()) {
                //TODO error handling
            }

            //Find available devices and send notifications to them
            List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
            for (Device device : userDevices) {
                sendNotification(device, notification);
            }
        }
    }


    private String getNotificationTitle(Language lng, Notification notification) {
        String title = null;
        switch (notification.getMessageTypeEnum()) {
            case REQUESTRIDE:
                title = languageUtils.getTranslation(lng, "notification.request_ride_title");
                break;
            case PASSENGERACCEPTED:
                title = languageUtils.getTranslation(lng, "notification.passenger_added_title");
                break;
            case REQUESTRIDEDECLINED:
                title = languageUtils.getTranslation(lng, "notification.request_declined_title");
                break;
            case PASSENGERREMOVED:
                title = languageUtils.getTranslation(lng, "notification.passenger_removed_title");
                break;
            case PASSENGERLEFT:
                title = languageUtils.getTranslation(lng, "notification.passenger_left_title");
                break;
            case RIDECANCELED:
                title = languageUtils.getTranslation(lng, "notification.ride_canceled_title");
                break;
            case RIDEOFFERED:
                title = languageUtils.getTranslation(lng, "notification.ride_offered_title");
                break;
            case NEWMESSAGE:
                title = languageUtils.getTranslation(lng, "notification.ride_newmessage_title");
                break;
            default:
                //TODO error handling
                break;
        }
        return title;
    }

    private String getNotificationMessage(Language lng, Notification notification) {
        String message = null;
        User user = null;
        switch (notification.getMessageTypeEnum()) {
            case REQUESTRIDE:
                message = languageUtils.getTranslation(lng, "notification.request_ride_body");
                user = userService.load(notification.getCreator());
                message = message.replace("$$user_name$$", user.getFirstName() + " " + user.getLastName()); //Replace with full name
                break;
            case PASSENGERACCEPTED:
                message = languageUtils.getTranslation(lng, "notification.passenger_added_body");
                user = userService.load(notification.getCreator());
                message = message.replace("$$user_name$$", user.getFirstName() + " " + user.getLastName()); //Replace with full name
                break;
            case REQUESTRIDEDECLINED:
                message = languageUtils.getTranslation(lng, "notification.request_declined_body");
                break;
            case PASSENGERREMOVED:
                message = languageUtils.getTranslation(lng, "notification.passenger_removed_body");
                user = userService.load(notification.getCreator());
                message = message.replace("$$user_name$$", user.getFirstName() + " " + user.getLastName()); //Replace with full name
                break;
            case PASSENGERLEFT:
                message = languageUtils.getTranslation(lng, "notification.passenger_left_body");
                user = userService.load(notification.getCreator());
                message = message.replace("$$user_name$$", user.getFirstName() + " " + user.getLastName()); //Replace with full name
                break;
            case RIDECANCELED:
                message = languageUtils.getTranslation(lng, "notification.ride_canceled_body");
                break;
            case RIDEOFFERED:
                message = languageUtils.getTranslation(lng, "notification.ride_offered_body");
                user = userService.load(notification.getCreator());
                message = message.replace("$$user_name$$", user.getFirstName() + " " + user.getLastName()); //Replace with full name
                break;
            case NEWMESSAGE:
                message = languageUtils.getTranslation(lng, "notification.ride_newmessage_body");
                break;
            default:
                //TODO error handling
                break;
        }
        return message;
    }

    //Determine if message was sent successfully and take actions if necessary
    private boolean handleGcmResponse(GcmResponse response, Device device, Notification notification) {
        if (response.hasErrors()) {
            return false;
        }
        if (response.hasMessageError()) {
            deviceService.markDisabled(device.getId());
            return false;
        }

        if (response.hasCanonicalId()) {                              //TODO: If canonical_id then search if exists another active device
            device.setToken(response.getCanonicalId());             //TODO: with the same canonical_id. If exists then make current one disabled
            device.setUpdatedAt(DateUtils.getCurrentDate());        //TODO: It means that there is another user on the device
            deviceDao.update(device);                               //TODO: or that the device updated the reg_id => duplicate
        }

        notification.setStatus(Notification.NotificationStatus.SEND);       //If sent then save notification as sent
        notification.setUpdatedAt(DateUtils.getCurrentDate());
        notificationDao.update(notification);
        return true;
    }

    private boolean sendNotification(Device device, Notification notification) {

        Language lng = Language.EN;     //TODO :  Currently supports only english
        String title = this.getNotificationTitle(lng, notification);
        String message = this.getNotificationMessage(lng, notification);
        boolean result = false;
        GcmResponse response;

        switch (device.getPlatform().toLowerCase()) {
            case "android":
                response = gcm.sendNotificationV2(device.getToken(), title, message, notification.getUserId(), notification.getRideId());
                result = this.handleGcmResponse(response, device, notification);
                break;
            case "ios":
                response = gcm.sendNotificationV2(device.getToken(), title, message, notification.getUserId(), notification.getRideId());
                result = this.handleGcmResponse(response, device, notification);
                break;
            case "windows":
                break;
        }
        return result;
    }


    public void notifyNewMessage(Integer rideId) {
        Notification notification = new Notification();


        Ride ride = rideService.load(rideId);
        if (ride == null) { // FIXME konstantinos
            return;
        }
        User creator = ride.getUser();
        if (creator == null) {
            return;
        }

        notification.setRideId(rideId);
        notification.setMessageType(Notification.NotificationType.NEWMESSAGE);
        notification.setCreator(creator.getId());
        notification.setMessage(this.getNotificationMessage(Language.EN, notification)); //Always save notif in English

        //Find all passengers and send them notification
        List<User> passengers = passengerService.getByRideId(rideId).getEntity();
        for (User receipient : passengers) {

            if (receipient.getId() == ride.getUser().getId()) {       //Do not send to the owner
                continue;
            }

            notification.setUserId(receipient.getId());
            if (this.create(notification).hasErrors()) {
                //TODO error handling
            }

            //Find available devices and send notifications to them
            List<Device> userDevices = deviceService.getEnabledDevicesByUserId(receipient.getId()).getEntity();
            for (Device device : userDevices) {
                sendNotification(device, notification);
            }
        }
    }

    @Override
    public ServiceResponse<Notification> update(Notification object) {
        return null;
    }

    @Override
    public ServiceResponse<Notification> delete(Notification object) {
        return null;
    }

    @Override
    public ServiceResponse<Notification> delete(Integer id) {
        return null;
    }
}
