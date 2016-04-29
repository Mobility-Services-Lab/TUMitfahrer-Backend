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

package de.tumitfahrer.dtos.ride.response;


import javax.xml.bind.annotation.XmlElement;


public class RideDeletedDTO {

    private int id;
    private String departurePlace;
    private String destination;
    private String departureTime;
    private String title;
    private String meetingPoint;
    private Double price;

    private String createdAt;
    private String updatedAt;
    private String car;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
