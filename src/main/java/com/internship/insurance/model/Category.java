package com.internship.insurance.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ColumnDefault(value="1")
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @JsonManagedReference
    private List<Property> properties;

    public Category() {
        this.properties = new ArrayList<>();
    }

    public Category(String title) {
        this.title = title;
        this.properties = new ArrayList<>();
    }

    public Category(String title, List<Property> properties) {
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

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
