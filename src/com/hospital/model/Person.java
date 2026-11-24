
package com.hospital.model;

import com.hospital.model.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Person {

    // Fields
    private Long personId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;

    // Constructor for new persons
    protected Person(String firstName, String lastName,
                     Gender gender, LocalDate dateOfBirth,
                     String phone, String email, String address) {

        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setPhone(phone);
        setEmail(email);
        setAddress(address);

        this.createdAt = LocalDateTime.now();
    }

    // Constructor for database mapping
    protected Person(Long personId, String firstName, String lastName,
                     Gender gender, LocalDate dateOfBirth,
                     String phone, String email, String address,
                     LocalDateTime createdAt) {

        if (personId == null || personId <= 0) {
            throw new IllegalArgumentException("Invalid person ID.");
        }

        this.personId = personId;

        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setPhone(phone);
        setEmail(email);
        setAddress(address);

        validateCreatedAt(createdAt);
        this.createdAt = createdAt;
    }

    // Getters
    public Long getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setFirstName(String firstName) {
        validateName(firstName, "First name");
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        validateName(lastName, "Last name");
        this.lastName = lastName.trim();
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }

        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid date of birth.");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = phone == null ? null : phone.trim();
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email == null ? null : email.trim().toLowerCase();
    }

    public void setAddress(String address) {
        if (address != null && address.trim().length() > 255) {
            throw new IllegalArgumentException("Address cannot exceed 255 characters.");
        }

        this.address = address == null ? null : address.trim();
    }

    // Validation Methods
    private void validateName(String name, String fieldName) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }

        if (name.trim().length() < 2 || name.trim().length() > 50) {
            throw new IllegalArgumentException(
                    fieldName + " must be between 2 and 50 characters.");
        }
    }

    private void validatePhone(String phone) {

        if (phone == null) {
            throw new IllegalArgumentException("Phone cannot be null.");
        }

        String trimmedPhone = phone.trim();

        if (trimmedPhone.length() < 7 || trimmedPhone.length() > 20) {
            throw new IllegalArgumentException(
                    "Phone must be between 7 and 20 characters.");
        }

        int startIndex = trimmedPhone.startsWith("+") ? 1 : 0;

        if (startIndex == trimmedPhone.length()) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        for (int i = startIndex; i < trimmedPhone.length(); i++) {
            if (!Character.isDigit(trimmedPhone.charAt(i))) {
                throw new IllegalArgumentException(
                        "Phone must contain only digits and an optional +.");
            }
        }
    }

    private void validateEmail(String email) {

        if (email == null) {
            return;
        }

        String trimmedEmail = email.trim();

        if (trimmedEmail.length() > 100
                || !trimmedEmail.contains("@")
                || !trimmedEmail.contains(".")
                || trimmedEmail.indexOf("@") == 0
                || trimmedEmail.lastIndexOf(".") < trimmedEmail.indexOf("@")
                || trimmedEmail.contains(" ")) {

            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    private void validateCreatedAt(LocalDateTime date) {

        if (date == null || date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid creation date.");
        }
    }

    // Business Methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // Object Methods
    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fullName='" + getFullName() + '\'' +
                ", gender=" + gender +
                ", dateOfBirth=" + dateOfBirth +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Person)) {
            return false;
        }

        Person person = (Person) obj;

        return personId != null && personId.equals(person.personId);
    }

    @Override
    public int hashCode() {
        return personId == null ? System.identityHashCode(this) : personId.hashCode();
    }
}
