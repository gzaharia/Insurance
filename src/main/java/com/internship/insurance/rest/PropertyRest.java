package com.internship.insurance.rest;

import com.internship.insurance.model.Property;
import com.internship.insurance.model.Status;
import com.internship.insurance.repository.PropertyRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        propertyRepo.save(property);
    }

    @PutMapping("properties/edit/{id}")
    public ResponseEntity<Property> editOneProperty(
            @PathVariable Long id,
            @RequestBody Property propertyDetails
    ) {
        Optional<Property> propertyFromDb = propertyRepo.findById(id);

        if (propertyFromDb.isPresent()) {
            BeanUtils.copyProperties(propertyDetails, propertyFromDb.get());
            propertyFromDb.get().setId(id);
            propertyFromDb.get().setStatus(propertyDetails.getStatus());
            propertyRepo.save(propertyFromDb.get());
            return ResponseEntity.ok(propertyFromDb.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("properties/delete/{id}")
    public ResponseEntity<Property> deleteOneProperty(@PathVariable Long id) {
        Optional<Property> propertyFromDb = propertyRepo.findById(id);
        if (propertyFromDb.isPresent()){
            propertyFromDb.get().setStatus(Status.DELETED);
            propertyRepo.save(propertyFromDb.get());
            return ResponseEntity.ok(propertyFromDb.get());
        }
        return ResponseEntity.badRequest().build();
    }
}
