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

package de.tumitfahrer.dtos.activity;

import de.tumitfahrer.util.DateUtils;

import javax.xml.bind.annotation.XmlElement;

public class BadgeDTO {

    private String timelineUpdatedAt;
    private String campusUpdatedAt;
    private String activityUpdatedAt;
    private String myRidesUpdatedAt;
    private String createdAt;
    private Integer timelineBadge;
    private Integer campusBadge;
    private Integer activityBadge;
    private Integer myRidesBadge;

    public BadgeDTO(Integer campusBadge, Integer activityBadge, Integer timelineBadge, Integer myRidesBadge, String campusUpdatedAt, String activityUpdatedAt, String timelineUpdatedAt, String myRidesUpdatedAt) {
        this.campusBadge = campusBadge;
        this.activityBadge = activityBadge;
        this.timelineBadge = timelineBadge;
        this.myRidesBadge = myRidesBadge;
        this.timelineUpdatedAt = timelineUpdatedAt;
        this.campusUpdatedAt = campusUpdatedAt;
        this.activityUpdatedAt = activityUpdatedAt;
        this.myRidesUpdatedAt = myRidesUpdatedAt;
        this.createdAt = DateUtils.getCurrentDate().toString();
    }

    @XmlElement(name = "timeline_updated_at")
    public String getTimelineUpdatedAt() {
        return timelineUpdatedAt;
    }

    @XmlElement(name = "timeline_updated_at")
    public void setTimelineUpdatedAt(String timelineUpdatedAt) {
        this.timelineUpdatedAt = timelineUpdatedAt;
    }

    @XmlElement(name = "campus_updated_at")
    public String getCampusUpdatedAt() {
        return campusUpdatedAt;
    }

    @XmlElement(name = "campus_updated_at")
    public void setCampusUpdatedAt(String campusUpdatedAt) {
        this.campusUpdatedAt = campusUpdatedAt;
    }

    @XmlElement(name = "my_rides_updated_at")
    public String getMyRidesUpdatedAt() {
        return myRidesUpdatedAt;
    }

    @XmlElement(name = "my_rides_updated_at")
    public void setMyRidesUpdatedAt(String myRidesUpdatedAt) {
        this.myRidesUpdatedAt = myRidesUpdatedAt;
    }

    @XmlElement(name = "activity_updated_at")
    public String getActivityUpdatedAt() {
        return activityUpdatedAt;
    }

    @XmlElement(name = "activity_updated_at")
    public void setActivityUpdatedAt(String activityUpdatedAt) {
        this.activityUpdatedAt = activityUpdatedAt;
    }

    @XmlElement(name = "created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @XmlElement(name = "created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @XmlElement(name = "campus_badge")
    public Integer getCampusBadge() {
        return campusBadge;
    }

    @XmlElement(name = "campus_badge")
    public void setCampusBadge(Integer campusBadge) {
        this.campusBadge = campusBadge;
    }

    @XmlElement(name = "timeline_badge")
    public Integer getTimelineBadge() {
        return timelineBadge;
    }

    @XmlElement(name = "timeline_badge")
    public void setTimelineBadge(Integer timelineBadge) {
        this.timelineBadge = timelineBadge;
    }

    @XmlElement(name = "activity_badge")
    public Integer getActivityBadge() {
        return activityBadge;
    }

    @XmlElement(name = "activity_badge")
    public void setActivityBadge(Integer activityBadge) {
        this.activityBadge = activityBadge;
    }

    @XmlElement(name = "my_rides_badge")
    public Integer getMyRidesBadge() {
        return myRidesBadge;
    }

    @XmlElement(name = "my_rides_badge")
    public void setMyRidesBadge(Integer myRidesBadge) {
        this.myRidesBadge = myRidesBadge;
    }
}