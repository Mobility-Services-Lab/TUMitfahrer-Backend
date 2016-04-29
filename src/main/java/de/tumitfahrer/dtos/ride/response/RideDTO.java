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

import de.tumitfahrer.dtos.rating.response.RatingDTO;
import de.tumitfahrer.dtos.request.response.RideRequestEntityDTO;
import de.tumitfahrer.dtos.user.response.RideOwnerDTO;
import de.tumitfahrer.dtos.user.response.UserDTO;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class RideDTO {

    private int id;
    private RideOwnerDTO user;
    private String departurePlace;
    private String destination;
    private String departureTime;
    private String title;
    private Integer freeSeats;
    private Integer freeSeatsCurrent;
    private Integer userId;
    private String meetingPoint;
    private Double price;
    private Integer rideType;
    private Double departureLatitude;
    private Double departureLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String createdAt;
    private String updatedAt;
    private String car;
    private Boolean isDriver;
    private Boolean isRideRequest;

    private Double realtimeKm;
    private String realtimeDepartureTime;
    private Boolean isFinished;
    private Integer regularRideId;
    private Integer conversationId;


    private List<UserDTO> passengers = new ArrayList<>();
    private List<RatingDTO> ratings = new ArrayList<>();
    private List<RideRequestEntityDTO> requests = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "ride_owner")
    public RideOwnerDTO getUser() {
        return user;
    }

    @XmlElement(name = "ride_owner")
    public void setUser(RideOwnerDTO user) {
        this.user = user;
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

    @XmlElement(name = "free_seats")
    public Integer getFreeSeats() {
        return freeSeats;
    }

    @XmlElement(name = "free_seats")
    public void setFreeSeats(Integer freeSeats) {
        this.freeSeats = freeSeats;
    }

    @XmlElement(name = "free_seats_current")
    public Integer getFreeSeatsCurrent() {
        return freeSeatsCurrent;
    }

    @XmlElement(name = "free_seats_current")
    public void setFreeSeatsCurrent(Integer freeSeatsCurrent) {
        this.freeSeatsCurrent = freeSeatsCurrent;
    }

    @XmlElement(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    @XmlElement(name = "user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
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

    @XmlElement(name = "is_ride_request")
    public Boolean getIsRideRequest() {
        return this.isRideRequest;
    }

    @XmlElement(name = "is_ride_request")
    public void setIsRideRequest(Boolean isRideRequest) {
        this.isRideRequest = isRideRequest;
    }

    public List<UserDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserDTO> passengers) {
        this.passengers = passengers;
    }

    public List<RatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingDTO> ratings) {
        this.ratings = ratings;
    }

    public List<RideRequestEntityDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<RideRequestEntityDTO> requests) {
        this.requests = requests;
    }

    @XmlElement(name = "realtime_km")
    public Double getRealtimeKm() {
        return realtimeKm;
    }

    @XmlElement(name = "realtime_km")
    public void setRealtimeKm(Double realtimeKm) {
        this.realtimeKm = realtimeKm;
    }

    @XmlElement(name = "realtime_departure_time")
    public String getRealtimeDepartureTime() {
        return realtimeDepartureTime;
    }

    @XmlElement(name = "realtime_departure_time")
    public void setRealtimeDepartureTime(String realtimeDepartureTime) {
        this.realtimeDepartureTime = realtimeDepartureTime;
    }

    @XmlElement(name = "is_finished")
    public Boolean getIsFinished() {
        return isFinished;
    }

    @XmlElement(name = "is_finished")
    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    @XmlElement(name = "regular_ride_id")
    public Integer getRegularRideId() {
        return regularRideId;
    }

    @XmlElement(name = "regular_ride_id")
    public void setRegularRideId(Integer regularRideId) {
        this.regularRideId = regularRideId;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

}
