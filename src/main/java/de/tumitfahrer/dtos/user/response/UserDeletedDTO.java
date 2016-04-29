package de.tumitfahrer.dtos.user.response;

import de.tumitfahrer.enums.UniversityDepartment;

import javax.xml.bind.annotation.XmlElement;

public class UserDeletedDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String car;
    private String email;
    private String phoneNumber;
    private UniversityDepartment department;
    private boolean isStudent;
    private String deletedAt;


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
    public boolean isStudent() {
        return isStudent;
    }

    @XmlElement(name = "student")
    public void setStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    @XmlElement(name = "deleted_at")
    public String getDeletedAt() {
        return deletedAt;
    }

    @XmlElement(name = "deleted_at")
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
