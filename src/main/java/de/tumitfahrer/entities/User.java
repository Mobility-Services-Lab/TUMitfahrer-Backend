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

import de.tumitfahrer.enums.IntendedUse;
import de.tumitfahrer.enums.UniversityDepartment;
import de.tumitfahrer.validation.annotations.WhitelistedEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "USERS")
public class User implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Length(min = 2, message = "{user.firstName.minLength}")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Length(min = 2, message = "{user.lastName.minLength}")
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotEmpty(message = "{user.email.notEmpty}")
    @Email(message = "{user.email.invalid}")
    @WhitelistedEmail
    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @NotNull(message = "{user.department.notEmpty}")
    @Column(name = "DEPARTMENT")
    @Enumerated(EnumType.STRING)
    private UniversityDepartment department;

    @Column(name = "CAR")
    private String car;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "SALT")
    private String salt;

    @Column(name = "API_KEY")
    private String apiKey;

    @Column(name = "API_KEY_EXPIRES")
    private Date apiKeyExpires;

    @Column(name = "ADMIN")
    private Boolean isAdmin;

    @Column(name = "IS_STUDENT")
    private Boolean isStudent;

    @Column(name = "RATING_AVG")
    private Double rating;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "AVATAR_ID")
    private Integer avatarId;

    @Column(name = "DELETED_AT", nullable = true)
    private Date deletedAt;

    @NotNull
    @Column(name = "INTENDED_USE", nullable = false)
    @Enumerated(EnumType.STRING)
    private IntendedUse intendedUse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UniversityDepartment getDepartment() {

        if (department == null) {
            return UniversityDepartment.UNKNOWN;
        }

        return department;
    }

    public void setDepartment(UniversityDepartment department) {
        this.department = department;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(Boolean isStudent) {
        this.isStudent = isStudent;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Date getApiKeyExpires() {
        return apiKeyExpires;
    }

    public void setApiKeyExpires(Date apiKeyExpires) {
        this.apiKeyExpires = apiKeyExpires;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public Boolean isStudent() {
        return isStudent;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getStudent() {
        return isStudent;
    }

    public void setStudent(Boolean isStudent) {
        this.isStudent = isStudent;
    }

    public IntendedUse getIntendedUse() {
        return intendedUse;
    }

    public void setIntendedUse(IntendedUse intendedUse) {
        this.intendedUse = intendedUse;
    }

    /*
    @Column(name = "DEPARTMENT", nullable = true)
	@Enumerated(EnumType.STRING)
	private UniversityDepartment userRight;
    */
}