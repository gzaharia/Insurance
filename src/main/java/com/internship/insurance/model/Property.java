package com.internship.insurance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Category category;

    private double coefficient;
    @ColumnDefault(value="0")
    @Column(nullable = false)
    private Status status;

    public Property() {
    }

    public Property(String title, Category category, double coefficient, Status status) {
        this.title = title;
        this.category = category;
        this.coefficient = coefficient;
        this.status = status;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getStatus() {
        return status.ordinal();
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
