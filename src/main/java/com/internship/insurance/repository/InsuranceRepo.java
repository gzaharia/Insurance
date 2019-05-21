package com.internship.insurance.repository;

import com.internship.insurance.model.Category;
import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Status;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceRepo extends JpaRepository<InsuranceOffer, Long> {

    InsuranceOffer getByTitle(String title);

    List<InsuranceOffer> getAllByStatus(Status status);
}
