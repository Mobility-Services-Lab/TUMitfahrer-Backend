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

import de.tumitfahrer.entities.validation.CreateRideGroup;
import de.tumitfahrer.entities.validation.RideGroup;
import de.tumitfahrer.util.DateUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RIDES")
public class Ride implements EntityInterface {

    public static final int RIDE_CAMPUS = 0;
    public static final int RIDE_ACTIVITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "rides_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotEmpty(message = "{ride.departurePlace.notEmpty}")
    @Column(name = "DEPARTURE_PLACE")
    private String departurePlace;

    @NotEmpty(message = "{ride.destination.notEmpty}")
    @Column(name = "DESTINATION")
    private String destination;

    @NotNull
    @Future(message = "departure date is in the past")
    @Column(name = "DEPARTURE_TIME")
    private Date departureTime;

    @Column(name = "TITLE")
    private String title;

    @Min.List({
            @Min(value = 0, groups = {RideGroup.class}),
            @Min(value = 1, message = "{ride.freeSeats.notNull}", groups = {CreateRideGroup.class})
    })
    @Column(name = "FREE_SEATS")
    private Integer freeSeats;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    private User user;

    @NotEmpty(message = "{ride.meetingPoint.notEmpty}")
    @Column(name = "MEETING_POINT")
    private String meetingPoint;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    @Column(name = "REALTIME_KM", nullable = true)
    private Double realtimeKm;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "REALTIME_DEPARTURE_TIME", nullable = true)
    private Date realtimeDepartureTime;

    @Column(name = "DURATION", nullable = true)
    private Double duration;

    @Column(name = "REALTIME_ARRIVAL_TIME", nullable = true)
    private Date realtimeArrivalTime;

    @Column(name = "IS_FINISHED")
    private Boolean isFinished;

    @NotNull(message = "{ride.rideType.NotNull}")
    @Column(name = "RIDE_TYPE")
    private Integer rideType;

    @NotNull(message = "{ride.departureLatitude.notNull}")
    @Column(name = "DEPARTURE_LATITUDE")
    private Double departureLatitude;

    @NotNull(message = "{ride.departureLongitude.notNull}")
    @Column(name = "DEPARTURE_LONGITUDE")
    private Double departureLongitude;

    @NotNull(message = "{ride.destinationLatitude.notNull}")
    @Column(name = "DESTINATION_LATITUDE")
    private Double destinationLatitude;

    @NotNull(message = "{ride.destinationLongitude.notNull}")
    @Column(name = "DESTINATION_LONGITUDE")
    private Double destinationLongitude;

    @Column(name = "CAR")
    private String car;

    @Column(name = "RATING_ID", nullable = true)
    private Integer ratingId;

    @Column(name = "LAST_CANCEL_TIME", nullable = true)
    private Date lastCancelTime;

    @Column(name = "REGULAR_RIDE_ID", nullable = true)
    private Integer regularRideId;

    @OneToMany(mappedBy = "rideId", targetEntity = Passenger.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Passenger> passengers;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(Integer freeSeats) {
        this.freeSeats = freeSeats;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return getDeletedAt() != null;
    }

    public Double getRealtimeKm() {
        return realtimeKm;
    }

    public void setRealtimeKm(Double realtimeKm) {
        this.realtimeKm = realtimeKm;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getRealtimeDepartureTime() {
        return realtimeDepartureTime;
    }

    public void setRealtimeDepartureTime(Date realtimeDepartureTime) {
        this.realtimeDepartureTime = realtimeDepartureTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Date getRealtimeArrivalTime() {
        return realtimeArrivalTime;
    }

    public void setRealtimeArrivalTime(Date realtimeArrivalTime) {
        this.realtimeArrivalTime = realtimeArrivalTime;
    }

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    public Double getDepartureLatitude() {
        return departureLatitude;
    }

    public void setDepartureLatitude(Double departureLatitude) {
        this.departureLatitude = departureLatitude;
    }

    public Double getDepartureLongitude() {
        return departureLongitude;
    }

    public void setDepartureLongitude(Double departureLongitude) {
        this.departureLongitude = departureLongitude;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Date getLastCancelTime() {
        return lastCancelTime;
    }

    public void setLastCancelTime(Date lastCancelTime) {
        this.lastCancelTime = lastCancelTime;
    }

    public Integer getRegularRideId() {
        return regularRideId;
    }

    public void setRegularRideId(Integer regularRideId) {
        this.regularRideId = regularRideId;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isPastRide() {
        return departureTime.before(DateUtils.getCurrentDate());
    }
}