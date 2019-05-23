package com.internship.insurance.repository;

import com.internship.insurance.model.Category;
import com.internship.insurance.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    @Query(value = "Select * from categories c where not status='DELETED' order by id", nativeQuery = true)
    List<Category> findAllActiveCategories();

}
