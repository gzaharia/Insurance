package com.internship.insurance.repository;

import com.internship.insurance.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    @Query(value = "Select * from categories c where status=1 or status is null order by id", nativeQuery = true)
    List<Category> findAllActiveCategories();
}
