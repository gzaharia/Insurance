package com.internship.insurance.rest;

import com.internship.insurance.model.Category;
import com.internship.insurance.repository.CategoryRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin
public class CategoryRest {
    private final CategoryRepo categoryRepo;

    public CategoryRest(CategoryRepo categoryRepo) {
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

    @PostMapping("categories/add")
    public Category addOneCategory(@RequestBody Category category) {
        categoryRepo.save(category);
        return category;
    }

    @PutMapping("categories/edit/{id}")
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

    @DeleteMapping("categories/delete/{id}")
    public void deleteOneCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
    }

}