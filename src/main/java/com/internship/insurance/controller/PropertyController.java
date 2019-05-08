package com.internship.insurance.controller;

import com.internship.insurance.model.Property;
import com.internship.insurance.repository.PropertyRepo;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admin")
public class PropertyController {
    private final PropertyRepo propertyRepo;

    public PropertyController(PropertyRepo propertyRepo) {
        this.propertyRepo = propertyRepo;
    }

    @GetMapping("properties")
    public List<Property> getAllProperties() {
        return propertyRepo.findAll();
    }

    @GetMapping("properties/{id}")
    public Property getOneProperty(@PathVariable Long id) throws NotFoundException {
        Optional<Property> propertyFromDb = propertyRepo.findById(id);

        if(propertyFromDb.isPresent()) {
            return propertyFromDb.get();
        }
        else {
            throw new NotFoundException("Property not found");
        }
    }

    @GetMapping("properties/{id}")
    public List<Property> getAllPropertiesForCategory(@PathVariable Long id) {
        List<Property> properties = new ArrayList<>();
        for(Property property : getAllProperties()) {
            if(property.getCategory().getId().equals(id)) {
                properties.add(property);
            }
        }
        return properties;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("properties/{id}")
    public void addProperty(@RequestBody Property property) {
        propertyRepo.save(property);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("properties/{id}")
    public ResponseEntity<Property> editOneProperty(
            @PathVariable Long id,
            @RequestBody Property propertyDetails)
            throws NotFoundException {
        Optional<Property> propertyFromDb = propertyRepo.findById(id);
        if(propertyFromDb.isPresent()) {
            Property property = propertyFromDb.get();
            property.setId(propertyDetails.getId());
            property.setCategory(propertyDetails.getCategory());
            property.setCoefficient(propertyDetails.getCoefficient());
            property.setTitle(propertyDetails.getTitle());
            return ResponseEntity.ok(propertyRepo.save(property));
        }
        else {
            throw new NotFoundException("Property not found");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public void deleteOneProperty(@PathVariable Long id) {
        propertyRepo.deleteById(id);
    }
}
