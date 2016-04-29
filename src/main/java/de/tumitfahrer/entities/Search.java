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
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "RIDE_SEARCHES")
public class Search implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "ride_searches_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DEPARTURE_PLACE", nullable = true)
    private String departurePlace;

    @Column(name = "DESTINATION", nullable = true)
    private String destination;

    @Column(name = "DEPARTURE_TIME", nullable = true)
    private Date departureTime;

    @NotNull
    @Column(name = "DEPARTURE_TIME_OFFSET_BEFORE", nullable = false)
    private Integer departureTimeOffsetBefore;

    @NotNull
    @Column(name = "DEPARTURE_TIME_OFFSET_AFTER", nullable = false)
    private Integer departureTimeOffsetAfter;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;

    @Column(name = "RIDE_TYPE")
    private Integer rideType;

    @Column(name = "DEPARTURE_THRESHOLD")
    private Double departurePlaceThreshold;

    @Column(name = "DESTINATION_THRESHOLD")
    private Double destinationThreshold;

    @Column(name = "DEPARTURE_LATITUDE")
    private Double departureLatitude;

    @Column(name = "DEPARTURE_LONGITUDE")
    private Double departureLongitude;

    @Column(name = "DESTINATION_LATITUDE")
    private Double destinationLatitude;

    @Column(name = "DESTINATION_LONGITUDE")
    private Double destinationLongitude;


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

    public Integer getDepartureTimeOffsetBefore() {
        return departureTimeOffsetBefore;
    }

    public void setDepartureTimeOffsetBefore(Integer departureTimeOffsetBefore) {
        this.departureTimeOffsetBefore = departureTimeOffsetBefore;
    }

    public Integer getDepartureTimeOffsetAfter() {
        return departureTimeOffsetAfter;
    }

    public void setDepartureTimeOffsetAfter(Integer departureTimeOffsetAfter) {
        this.departureTimeOffsetAfter = departureTimeOffsetAfter;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    public Double getDeparturePlaceThreshold() {
        return departurePlaceThreshold;
    }

    public void setDeparturePlaceThreshold(Double departurePlaceThreshold) {
        this.departurePlaceThreshold = departurePlaceThreshold;
    }

    public Double getDestinationThreshold() {
        return destinationThreshold;
    }

    public void setDestinationThreshold(Double destinationThreshold) {
        this.destinationThreshold = destinationThreshold;
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

    /**
     * Checks whether all necessary parameters for a radius search are set
     *
     * @return
     */
    public boolean hasLatLong() {
        return departureLatitude != null && departureLongitude != null
                && destinationLatitude != null && destinationLongitude != null;
    }

    /**
     * Returns the earliest departure time composed out of the departureTime and the departureTimeOffsetBefore
     *
     * @return The earliest departure time
     * @throws IllegalArgumentException if departureTimeOffsetBefore is negative or null
     */
    public Date getEarliestDepartureTime() {

        if (departureTimeOffsetBefore == null || departureTimeOffsetBefore < 0) {
            throw new IllegalArgumentException("departure time offset before must be a positive integer");
        }

        Date earliestDate = new Date(departureTime.getTime() - departureTimeOffsetBefore * 60000);
        return earliestDate;
    }

    /**
     * Returns the latest departure time composed out of the departureTime and the departureTimeOffsetAfter
     *
     * @return The latest departure time
     * @throws IllegalArgumentException if departureTimeOffsetAfter is negative or null
     */
    public Date getLatestDepartureTime() {

        if (departureTimeOffsetAfter == null || departureTimeOffsetAfter < 0) {
            throw new IllegalArgumentException("departe time offset after must be a positive integer");
        }

        Date latestDate = new Date(departureTime.getTime() + departureTimeOffsetAfter * 60000);
        return latestDate;
    }
}
