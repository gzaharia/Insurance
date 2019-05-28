package com.internship.insurance.dto;

import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Property;

import java.util.Set;

public class OrderDto {
    private Set<Property> properties;
    private InsuranceOffer insurance;

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
