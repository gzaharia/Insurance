package com.internship.insurance.dto;

import com.internship.insurance.model.Category;
import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Property;
import com.internship.insurance.model.Status;

import java.util.Set;

public class CategoryDto {


    private Long id;
    private String title;
    private String insuranceTitle;
    private Set<Property> properties;
    private String status;
//    private boolean deleted;

    public CategoryDto(){}

    public CategoryDto(Long id, String title, String insuranceTitle) {
        this.id = id;
        this.title = title;
        this.insuranceTitle = insuranceTitle;
    }

    public CategoryDto(Long id, String title, String insuranceTitle, Set<Property> properties, String status) {
        this.id = id;
        this.title = title;
        this.insuranceTitle = insuranceTitle;
        this.properties = properties;
        this.status = status;
//        this.deleted = deleted;
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

    public String getInsuranceTitle() {
        return insuranceTitle;
    }

    public void setInsuranceTitle(String insuranceTitle) {
        this.insuranceTitle = insuranceTitle;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public boolean isDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        this.deleted = deleted;
//    }
}
