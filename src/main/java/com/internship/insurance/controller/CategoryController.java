package com.internship.insurance.controller;

import com.internship.insurance.model.Category;
import com.internship.insurance.repository.CategoryRepo;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admin")
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
    public void addOneCategory(@RequestBody Category category) {
        categoryRepo.save(category);
    }

    @PutMapping("categories/{id}")
    public ResponseEntity<Category> editOneCategory(
            @PathVariable Long id,
            @RequestBody Category categoryDetails)
            throws NotFoundException {
        Optional<Category> categoryFromDb = categoryRepo.findById(id);
        if (categoryFromDb.isPresent()) {
            Category category = categoryFromDb.get();
            category.setId(categoryDetails.getId());
            category.setTitle(categoryDetails.getTitle());
            return ResponseEntity.ok(categoryRepo.save(category));
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @DeleteMapping("categories/{id}")
    public void deleteOneCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
    }

}
