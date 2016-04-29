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

package de.tumitfahrer.dtos.search;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import de.tumitfahrer.dtos.http.RequestDTO;
import de.tumitfahrer.util.DateUtils;

import javax.xml.bind.annotation.XmlElement;

@ApiModel
public class SearchRequestDTO extends RequestDTO {

    private String departurePlace;
    private Double departurePlaceThreshold;

    private String destination;
    private Double destinationThreshold;

    private String departureTime;

    private Integer departureTimeOffsetBefore = 0;
    private Integer departureTimeOffsetAfter = 1440;

    @ApiModelProperty(dataType = "integer", allowableValues = "0, 1")
    private Integer rideType;

    private Double departureLatitude;
    private Double departureLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;

    public SearchRequestDTO() {
        departureTime = DateUtils.convertCurrentDate();
    }

    @XmlElement(name = "departure_place")
    public String getDeparturePlace() {
        return departurePlace;
    }

    @XmlElement(name = "departure_place")
    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    @XmlElement(name = "departure_place_threshold")
    public Double getDeparturePlaceThreshold() {
        return departurePlaceThreshold;
    }

    @XmlElement(name = "departure_place_threshold")
    public void setDeparturePlaceThreshold(Double departurePlaceThreshold) {
        this.departurePlaceThreshold = departurePlaceThreshold;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @XmlElement(name = "destination_threshold")
    public Double getDestinationThreshold() {
        return destinationThreshold;
    }

    @XmlElement(name = "destination_threshold")
    public void setDestinationThreshold(Double destinationThreshold) {
        this.destinationThreshold = destinationThreshold;
    }

    @XmlElement(name = "departure_time")
    public String getDepartureTime() {
        return departureTime;
    }

    @XmlElement(name = "departure_time")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @XmlElement(name = "ride_type", required = true)
    public Integer getRideType() {
        return rideType;
    }

    @XmlElement(name = "ride_type", required = true)
    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    @XmlElement(name = "departure_latitude")
    public Double getDepartureLatitude() {
        return departureLatitude;
    }

    @XmlElement(name = "departure_latitude")
    public void setDepartureLatitude(Double departureLatitude) {
        this.departureLatitude = departureLatitude;
    }

    @XmlElement(name = "departure_longitude")
    public Double getDepartureLongitude() {
        return departureLongitude;
    }

    @XmlElement(name = "departure_longitude")
    public void setDepartureLongitude(Double departureLongitude) {
        this.departureLongitude = departureLongitude;
    }

    @XmlElement(name = "destination_latitude")
    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    @XmlElement(name = "destination_latitude")
    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    @XmlElement(name = "destination_longitude")
    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    @XmlElement(name = "destination_longitude")
    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    @XmlElement(name = "departure_time_offset_before")
    public Integer getDepartureTimeOffsetBefore() {
        return departureTimeOffsetBefore;
    }

    @XmlElement(name = "departure_time_offset_before")
    public void setDepartureTimeOffsetBefore(Integer departureTimeOffsetBefore) {
        this.departureTimeOffsetBefore = departureTimeOffsetBefore;
    }

    @XmlElement(name = "departure_time_offset_after")
    public Integer getDepartureTimeOffsetAfter() {
        return departureTimeOffsetAfter;
    }

    @XmlElement(name = "departure_time_offset_after")
    public void setDepartureTimeOffsetAfter(Integer departureTimeOffsetAfter) {
        this.departureTimeOffsetAfter = departureTimeOffsetAfter;
    }
}
