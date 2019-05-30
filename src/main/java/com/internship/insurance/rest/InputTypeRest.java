package com.internship.insurance.rest;

import com.internship.insurance.model.InputType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;

@RestController
@CrossOrigin
@RequestMapping("/api/inputTypes")
public class InputTypeRest {

    @GetMapping
    public ResponseEntity<EnumSet<InputType>> getAllInputTypes() {
        return ResponseEntity.ok(EnumSet.allOf(InputType.class));
    }
}
