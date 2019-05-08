package com.internship.insurance.controller;

import com.internship.insurance.model.Employee;
import com.internship.insurance.repository.EmployeeRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminController(EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @GetMapping("/users/{id}")
    public Employee getOneEmployee(@PathVariable Long id) throws NotFoundException {
        Optional<Employee> employeeFromDb = employeeRepo.findById(id);

        if (employeeFromDb.isPresent())
            return employeeFromDb.get();
        else
            throw new NotFoundException("Employee not found.");
    }

    @PostMapping("/users/add")
    public Employee addOneEmployee(@RequestBody Employee employee) {
        Employee newEmployee = new Employee();
        BeanUtils.copyProperties(employee, newEmployee);

        newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepo.save(newEmployee);
        return employee;
    }
}
