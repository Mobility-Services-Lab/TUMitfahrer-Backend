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

package de.tumitfahrer.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "notifications_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @PrimaryKeyJoinColumn
    @Column(name = "USER_ID", nullable = false)
    private Integer userId;
    @PrimaryKeyJoinColumn
    @Column(name = "RIDE_ID", nullable = false)
    private Integer rideId;
    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
    @Column(name = "STATUS", nullable = false)
    private String status;
    @Column(name = "MESSAGE", nullable = false)
    private String message;
    @Column(name = "EXTRA", nullable = true)                //Used to store 2nd userId for notifications
    private Integer creator;
    @Column(name = "DATE_TIME", nullable = true)
    private Date timeStamp;
    @Column(name = "MESSAGE_TYPE", nullable = false)
    private String messageType;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.setStatus(status.toString());
    }

    public NotificationStatus getStatusEnum() {
        return NotificationStatus.valueOf(status);                                 //todo
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMessageType() {                                //TODO
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public NotificationType getMessageTypeEnum() {                                //TODO
        return NotificationType.valueOf(messageType);
    }

    public void setMessageType(NotificationType notificationType) {
        this.messageType = notificationType.toString();
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum NotificationType {

        REQUESTRIDE(0),                     //Request to join ride                  - Inform ride owner
        PASSENGERACCEPTED(1),               //Request accepted - Passenger added    - Inform passenger
        REQUESTRIDEDECLINED(2),             //Request to join declined              - Inform requester
        PASSENGERREMOVED(3),                //Passenger removed by owner            - Inform passenger
        RIDECANCELED(4),                    //Ride canceled                         - Inform passengers
        PASSENGERLEFT(5),                //Passenger left ride                   - Inform ride owner
        RIDEOFFERED(6),
        NEWMESSAGE(7),                      //New message arrived in conversation    - Inform Passengers
        UNDEFINED(-1);                      //Just in case !

        private int _value;

        NotificationType(int Value) {
            this._value = Value;
        }

        public static NotificationType fromInt(int i) {
            for (NotificationType b : NotificationType.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return NotificationType.UNDEFINED;
        }

        public int getValue() {
            return _value;
        }
    }

    public enum NotificationStatus {
        SEND, NOTSEND
    }
}
