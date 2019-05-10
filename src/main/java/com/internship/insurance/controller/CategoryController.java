package com.internship.insurance.controller;

import com.internship.insurance.model.Category;
import com.internship.insurance.repository.CategoryRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin
public class CategoryController {
    private final CategoryRepo categoryRepo;

    public CategoryController(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("categories")
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @GetMapping("categories/{id}")
    public Category getOneCategory(@PathVariable Long id) throws NotFoundException {
        Optional<Category> categoryFromDb = categoryRepo.findById(id);
        if (categoryFromDb.isPresent()) {
            return categoryFromDb.get();
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("categories/add")
    public Category addOneCategory(@RequestBody Category category) {
        categoryRepo.save(category);
        return category;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("categories/{id}")
    public ResponseEntity<Category> editOneCategory(
            @PathVariable Long id,
            @RequestBody Category categoryDetails
    ) throws NotFoundException {
        Optional<Category> categoryFromDb = categoryRepo.findById(id);
        if (categoryFromDb.isPresent()) {
            BeanUtils.copyProperties(categoryDetails, categoryFromDb.get());
            categoryFromDb.get().setId(id);
            categoryRepo.save(categoryFromDb.get());
            return ResponseEntity.ok(categoryFromDb.get());
        } else {
            throw new NotFoundException("Category not found!");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("categories/{id}")
    public void deleteOneCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
    }

}
