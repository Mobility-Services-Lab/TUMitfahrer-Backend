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

package de.tumitfahrer.dtos.user.response;

import de.tumitfahrer.dtos.ride.response.RideDTO;
import de.tumitfahrer.enums.IntendedUse;
import de.tumitfahrer.enums.UniversityDepartment;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String car;
    private String email;
    private String phoneNumber;
    private UniversityDepartment department;
    private Boolean isStudent;
    private Double rating;
    private Integer avatarId;
    private Integer ridesAsDriver;
    private Integer ridesAsPassenger;
    private List<RideDTO> rides;
    private String deletedAt;
    private IntendedUse intendedUse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @XmlElement(name = "first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlElement(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @XmlElement(name = "last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @XmlElement(name = "phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UniversityDepartment getDepartment() {
        return department;
    }

    public void setDepartment(UniversityDepartment department) {
        this.department = department;
    }

    @XmlElement(name = "student")
    public Boolean isStudent() {
        return isStudent;
    }

    @XmlElement(name = "student")
    public void setStudent(Boolean isStudent) {
        this.isStudent = isStudent;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<RideDTO> getRides() {
        return rides;
    }

    public void setRides(List<RideDTO> rides) {
        this.rides = rides;
    }

    @XmlElement(name = "avatar_id")
    public Integer getAvatarId() {
        return avatarId;
    }

    @XmlElement(name = "avatar_id")
    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    @XmlElement(name = "rides_as_driver")
    public Integer getRidesAsDriver() {
        return ridesAsDriver;
    }

    @XmlElement(name = "rides_as_driver")
    public void setRidesAsDriver(Integer ridesAsDriver) {
        this.ridesAsDriver = ridesAsDriver;
    }

    @XmlElement(name = "rides_as_passenger")
    public Integer getRidesAsPassenger() {
        return ridesAsPassenger;
    }

    @XmlElement(name = "rides_as_passenger")
    public void setRidesAsPassenger(Integer ridesAsPassenger) {
        this.ridesAsPassenger = ridesAsPassenger;
    }


    @XmlElement(name = "deleted_at")
    public String getDeletedAt() {
        return deletedAt;
    }

    @XmlElement(name = "deleted_at")
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    @XmlElement(name = "intented_use")
    public IntendedUse getIntendedUse() {
        return intendedUse;
    }

    @XmlElement(name = "intented_use")
    public void setIntendedUse(IntendedUse intendedUse) {
        this.intendedUse = intendedUse;
    }
}
