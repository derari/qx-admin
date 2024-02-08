package org.cthul.qxadmin.it.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class Author {

    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getName() {
        if (firstName == null || firstName.isBlank()) return lastName;
        if (lastName == null || lastName.isBlank()) return firstName;
        return firstName + " " + lastName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    void prePersist() {
        updatedAt = OffsetDateTime.now();
        if (createdAt == null) createdAt = updatedAt;
    }
}
