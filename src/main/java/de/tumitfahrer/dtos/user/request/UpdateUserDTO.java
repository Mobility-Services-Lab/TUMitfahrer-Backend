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

package de.tumitfahrer.dtos.user.request;

import com.wordnik.swagger.annotations.ApiModelProperty;
import de.tumitfahrer.enums.IntendedUse;
import de.tumitfahrer.enums.UniversityDepartment;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;

public class UpdateUserDTO {

    private String phoneNumber;
    private String car;
    private UniversityDepartment department;
    private IntendedUse intendedUse = IntendedUse.UNSPECIFIED;

    @ApiModelProperty(required = true)
    @NotNull
    private String firstName;

    @ApiModelProperty(required = true)
    @NotNull
    private String lastName;

    @XmlElement(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @XmlElement(name = "phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public UniversityDepartment getDepartment() {
        return department;
    }

    public void setDepartment(UniversityDepartment department) {
        this.department = department;
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

    @XmlElement(name = "intended_use")
    public IntendedUse getIntendedUse() {
        return intendedUse;
    }

    @XmlElement(name = "intended_use")
    public void setIntendedUse(IntendedUse intendedUse) {
        this.intendedUse = intendedUse;
    }
}