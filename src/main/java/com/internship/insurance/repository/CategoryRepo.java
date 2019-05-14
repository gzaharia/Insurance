package com.internship.insurance.repository;

import com.internship.insurance.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    @Query(value = "Select * from categories c where status=1 or status is null", nativeQuery = true)
    List<Category> findAllActiveCategories();

}
