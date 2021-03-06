package com.internship.insurance.rest;

import com.internship.insurance.model.Role;
import com.internship.insurance.repository.RoleRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@CrossOrigin
public class RoleRest {

    private final RoleRepo roleRepo;

    public RoleRest(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepo.findAll());
    }
}
