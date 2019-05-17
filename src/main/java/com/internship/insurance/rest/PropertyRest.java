package com.internship.insurance.rest;

import com.internship.insurance.model.Property;
import com.internship.insurance.model.Status;
import com.internship.insurance.repository.PropertyRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin
public class PropertyRest {
    private final PropertyRepo propertyRepo;

    public PropertyRest(PropertyRepo propertyRepo) {
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

    @PostMapping("properties/add")
    public void addProperty(@RequestBody Property property) {
        property.setStatus(Status.ACTIVE);
        propertyRepo.save(property);
    }

    @PutMapping("properties/edit/{id}")
    public ResponseEntity<Property> editOneProperty(
            @PathVariable Long id,
            @RequestBody Property propertyDetails
    ) throws NotFoundException {
        Optional<Property> propertyFromDb = propertyRepo.findById(id);

        if (propertyFromDb.isPresent()) {
            BeanUtils.copyProperties(propertyDetails, propertyFromDb.get());
            propertyFromDb.get().setId(id);
            propertyRepo.save(propertyFromDb.get());
            return ResponseEntity.ok(propertyFromDb.get());
        } else {
            throw new NotFoundException("Property not found!");
        }
    }

    @DeleteMapping("properties/delete/{id}")
    public void deleteOneProperty(@PathVariable Long id) {
        propertyRepo.deleteById(id);
    }
}
