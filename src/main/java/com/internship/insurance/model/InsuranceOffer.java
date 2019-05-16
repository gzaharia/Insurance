package com.internship.insurance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "insurance_offer")
public class InsuranceOffer extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "base_price")
    private Double basePrice;

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
