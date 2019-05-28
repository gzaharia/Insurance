package com.internship.insurance.rest;

import com.internship.insurance.model.Property;
import com.internship.insurance.model.Status;
import com.internship.insurance.repository.PropertyRepo;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PropertyRest(PropertyRepo propertyRepo) {
        this.propertyRepo = propertyRepo;
    }

    @GetMapping("properties")
    public List<Property> getAllProperties() {
        return propertyRepo.findAll();
    }

    @GetMapping("properties/{id}")
    public ResponseEntity<Property> getOneProperty(@PathVariable Long id) {
        Optional<Property> propertyFromDb = propertyRepo.findById(id);

        if(propertyFromDb.isPresent()) {
            return ResponseEntity.ok(propertyFromDb.get());
        }
        else {
            logger.error("Error: " + "Property not found. Id: " + id);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("properties/add")
    public ResponseEntity<Property> addProperty(@RequestBody Property property) {
        return ResponseEntity.ok(propertyRepo.save(property));
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
