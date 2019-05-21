package com.internship.insurance.rest;

import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Status;
import com.internship.insurance.repository.InsuranceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class InsuranceRest {

    private final InsuranceRepo insuranceRepo;

    public InsuranceRest(InsuranceRepo insuranceRepo) {
        this.insuranceRepo = insuranceRepo;
    }

    @GetMapping("/insurances/all")
    public ResponseEntity<List<InsuranceOffer>> getAllInsurances() {
        return ResponseEntity.ok(insuranceRepo.findAll());
    }

    @GetMapping("/insurances")
    public ResponseEntity<List<InsuranceOffer>> getAllActive() {
        return ResponseEntity.ok(insuranceRepo.getAllByStatus(Status.ACTIVE));
    }

    @GetMapping("/insurances/{id}")
    public ResponseEntity<InsuranceOffer> getOneInsurance(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceRepo.findById(id).orElse(null));
    }

    @GetMapping("/insurances/name/{name}")
    public ResponseEntity<InsuranceOffer> getOneInsuranceByUsername(@PathVariable String name) {
        InsuranceOffer insuranceOffer = insuranceRepo.getByTitle(name);
        return ResponseEntity.ok(insuranceOffer);
    }

    @PostMapping("/insurances/add/")
    public ResponseEntity<InsuranceOffer>addNewInsurance(@RequestBody InsuranceOffer insuranceOffer){
        return ResponseEntity.ok(insuranceRepo.save(insuranceOffer));
    }


    @PutMapping("/insurances/edit/{id}")
    public ResponseEntity<InsuranceOffer> editOneInsurance(
            @PathVariable Long id,
            @RequestBody InsuranceOffer insuranceOffer
    ) {
        Optional<InsuranceOffer> updateInsuranceOffer = insuranceRepo.findById(id);
        if (updateInsuranceOffer.isPresent()){
            updateInsuranceOffer.get().setStatus(insuranceOffer.getStatus());
            updateInsuranceOffer.get().setTitle(insuranceOffer.getTitle());
            updateInsuranceOffer.get().setBasePrice(insuranceOffer.getBasePrice());
            return ResponseEntity.ok(insuranceRepo.save(updateInsuranceOffer.get()));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/insurances/delete/{id}")
    public ResponseEntity<InsuranceOffer> deleteOneInsurance(@PathVariable Long id){
        Optional<InsuranceOffer> updateInsuranceOffer = insuranceRepo.findById(id);
        if (updateInsuranceOffer.isPresent()){
            updateInsuranceOffer.get().setStatus(Status.DELETED);
            return ResponseEntity.ok(insuranceRepo.save(updateInsuranceOffer.get()));
        }
        return ResponseEntity.badRequest().build();
    }
}
