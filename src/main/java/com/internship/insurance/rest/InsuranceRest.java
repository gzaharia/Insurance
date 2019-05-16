package com.internship.insurance.rest;

import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.repository.InsuranceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InsuranceRest {

    private final InsuranceRepo insuranceRepo;

    public InsuranceRest(InsuranceRepo insuranceRepo) {
        this.insuranceRepo = insuranceRepo;
    }

    @GetMapping("/insurance")
    public ResponseEntity<List<InsuranceOffer>> getAllInsurances() {
        return ResponseEntity.ok(insuranceRepo.findAll());
    }

    @GetMapping("/insurance/{id}")
    public ResponseEntity<InsuranceOffer> getOneInsurance(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceRepo.findById(id).orElse(null));
    }

    @GetMapping("/insurance/name/{name}")
    public ResponseEntity<InsuranceOffer> getOneInsuranceByUsername(@PathVariable String name) {
        InsuranceOffer insuranceOffer = insuranceRepo.getByTitle(name);
        return ResponseEntity.ok(insuranceOffer);
    }

    @PutMapping("/insurance/edit/{id}")
    public ResponseEntity<InsuranceOffer> editOneInsurance(
            @PathVariable Long id,
            @RequestBody InsuranceOffer insuranceOffer
    ) {
        InsuranceOffer newInsuranceOffer = new InsuranceOffer();
        BeanUtils.copyProperties(insuranceOffer, newInsuranceOffer);
        newInsuranceOffer.setId(id);
        return ResponseEntity.ok(insuranceRepo.save(newInsuranceOffer));
    }
}
