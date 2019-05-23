package com.internship.insurance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String title;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.REFRESH,
                    CascadeType.MERGE,
                    CascadeType.REMOVE
            },
            orphanRemoval = true)
    @OrderBy("id ASC")
    @JsonManagedReference
    private Set<Property> properties;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "insurance_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private InsuranceOffer insurance;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public InsuranceOffer getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceOffer insurance) {
        this.insurance = insurance;
    }
}
