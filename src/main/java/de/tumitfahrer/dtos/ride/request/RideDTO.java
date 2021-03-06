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

package de.tumitfahrer.dtos.ride.request;

import javax.xml.bind.annotation.XmlElement;

public class RideDTO {

    private String departurePlace;
    private String destination;
    private String departureTime;
    private String title;
    private Integer freeSeats;
    private String meetingPoint;
    private Double price;
    private Integer rideType;
    private Double departureLatitude;
    private Double departureLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String car;
    private Boolean isDriver;
    private Boolean isRideRequest;

    @XmlElement(name = "departure_place")
    public String getDeparturePlace() {
        return departurePlace;
    }

    @XmlElement(name = "departure_place")
    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @XmlElement(name = "departure_time")
    public String getDepartureTime() {
        return departureTime;
    }

    @XmlElement(name = "departure_time")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "free_seats")
    public Integer getFreeSeats() {
        return freeSeats;
    }

    @XmlElement(name = "free_seats")
    public void setFreeSeats(Integer freeSeats) {
        this.freeSeats = freeSeats;
    }

    @XmlElement(name = "meeting_point")
    public String getMeetingPoint() {
        return meetingPoint;
    }

    @XmlElement(name = "meeting_point")
    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @XmlElement(name = "ride_type")
    public Integer getRideType() {
        return rideType;
    }

    @XmlElement(name = "ride_type")
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

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @XmlElement(name = "is_ride_request")
    public Boolean getIsRideRequest() {
        return this.isRideRequest;
    }

    @XmlElement(name = "is_ride_request")
    public void setIsRideRequest(Boolean isRideRequest) {
        this.isRideRequest = isRideRequest;
    }
}
