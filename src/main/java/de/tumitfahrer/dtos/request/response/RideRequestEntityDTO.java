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

package de.tumitfahrer.dtos.request.response;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class represents a {@see de.tumitfahrer.entity.Request} entity's DTO.
 * The name contains the string "Entity" to let the docs work correctly.
 */
public class RideRequestEntityDTO {

    private Integer id;
    private Integer userId;
    private Integer rideId;
    private String createdAt;
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement(name = "passenger_id")
    public Integer getUserId() {
        return userId;
    }

    @XmlElement(name = "passenger_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @XmlElement(name = "ride_id")
    public Integer getRideId() {
        return rideId;
    }

    @XmlElement(name = "ride_id")
    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    @XmlElement(name = "created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @XmlElement(name = "created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @XmlElement(name = "updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @XmlElement(name = "updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
