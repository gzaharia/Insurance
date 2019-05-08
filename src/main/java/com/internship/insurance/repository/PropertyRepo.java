package com.internship.insurance.repository;

import com.internship.insurance.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepo extends JpaRepository<Property, Long> {
}
