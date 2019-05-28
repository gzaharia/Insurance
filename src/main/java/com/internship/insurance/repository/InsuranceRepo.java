package com.internship.insurance.repository;

import com.internship.insurance.dto.InsuranceCategoryDto;
import com.internship.insurance.model.Category;
import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Status;
import org.hibernate.annotations.Where;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.Set;

public interface InsuranceRepo extends JpaRepository<InsuranceOffer, Long> {

    InsuranceOffer getByTitle(String title);

    Set<InsuranceOffer> getAllByStatusOrderByTitleAsc(Status status);
}
