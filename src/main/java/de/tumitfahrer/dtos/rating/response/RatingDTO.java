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

package de.tumitfahrer.dtos.rating.response;

import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

public class RatingDTO {

    private Integer id;
    private Integer toUserId;
    private Integer fromUserId;
    private Integer ratingType;
    private Integer rideId;
    private Date createdAt;
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement(name = "to_user_id")
    public Integer getToUserId() {
        return toUserId;
    }

    @XmlElement(name = "to_user_id")
    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    @XmlElement(name = "from_user_id")
    public Integer getFromUserId() {
        return fromUserId;
    }

    @XmlElement(name = "from_user_id")
    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    @XmlElement(name = "ride_id")
    public Integer getRideId() {
        return rideId;
    }

    @XmlElement(name = "ride_id")
    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    @XmlElement(name = "rating_type")
    public Integer getRatingType() {
        return ratingType;
    }

    @XmlElement(name = "rating_type")
    public void setRatingType(Integer ratingType) {
        this.ratingType = ratingType;
    }

    @XmlElement(name = "created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @XmlElement(name = "created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlElement(name = "updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @XmlElement(name = "updated_at")
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
