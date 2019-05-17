package com.internship.insurance.repository;

import com.internship.insurance.model.InsuranceOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepo extends JpaRepository<InsuranceOffer, Long> {

    InsuranceOffer getByTitle(String title);
}
