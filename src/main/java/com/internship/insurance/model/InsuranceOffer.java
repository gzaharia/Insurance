package com.internship.insurance.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "insurance_offer")
public class InsuranceOffer extends BaseEntity {

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "base_price")
    private Double basePrice;
    @OneToMany(mappedBy = "insurance", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @JsonManagedReference
    private Set<Category> categories;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}
