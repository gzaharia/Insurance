package com.internship.insurance.rest;

import com.internship.insurance.dto.CategoryDto;
import com.internship.insurance.model.Category;
import com.internship.insurance.model.Status;
import com.internship.insurance.repository.CategoryRepo;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("categories/all")
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }


    @GetMapping("categories/insurances")
    public List<CategoryDto> getAllInsuranceCategories() {
        List<CategoryDto> categoryDtos = new ArrayList<>();
         categoryRepo.findAll().forEach( c -> {
             categoryDtos.add(new CategoryDto(c.getId(),c.getTitle(), c.getInsurance().getTitle(),c.getProperties(),c.getStatus().toString()));
             // new CategoryDto(c.getId(),c.getTitle(), c.getInsurance().getTitle()
         });

         return  categoryDtos;
    }

    @GetMapping("categories")
    public List<Category> getAllActiveCategories() {
        return categoryRepo.findAllActiveCategories();
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
    ) {
        Optional<Category> categoryFromDb = categoryRepo.findById(id);
        if (categoryFromDb.isPresent()) {
            categoryFromDb.get().setId(id);
            categoryFromDb.get().setTitle(categoryDetails.getTitle());
            categoryFromDb.get().setStatus(categoryDetails.getStatus());
            categoryFromDb.get().setInsurance(categoryDetails.getInsurance());
            categoryRepo.save(categoryFromDb.get());
            return ResponseEntity.ok(categoryFromDb.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("categories/delete/{id}")
    public ResponseEntity<Category> deleteOneCategory(@PathVariable Long id) {
        Optional<Category> categoryDelete = categoryRepo.findById(id);
        if (categoryDelete.isPresent()) {
            categoryDelete.get().setStatus(Status.DELETED);
            categoryRepo.save(categoryDelete.get());
            return ResponseEntity.ok(categoryDelete.get());
        }

        return ResponseEntity.badRequest().build();
    }

}
