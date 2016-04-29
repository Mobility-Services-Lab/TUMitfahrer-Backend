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

package de.tumitfahrer.dtos.rating.request;

import de.tumitfahrer.dtos.http.RequestDTO;

import javax.xml.bind.annotation.XmlElement;

public class RatingRequestDTO extends RequestDTO {

    private Integer toUserId;
    private Integer rideId;
    private Integer ratingType;

    @XmlElement(name = "to_user_id")
    public Integer getToUserId() {
        return toUserId;
    }

    @XmlElement(name = "to_user_id")
    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
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
}
