package com.internship.insurance.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(nullable = false,columnDefinition = "int default 1")
    private Status status;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @JsonManagedReference
    private Set<Property> properties;

    public Category() {
        this.properties = new HashSet<>();
    }

    public Category(String title) {
        this.title = title;
        this.properties = new HashSet<>();
    }

    public Category(String title, Set<Property> properties) {
        this.title = title;
        this.properties = properties;
        this.status = Status.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status.ordinal();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }
}
