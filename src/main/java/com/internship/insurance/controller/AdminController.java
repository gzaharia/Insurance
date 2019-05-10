package com.internship.insurance.controller;

import com.internship.insurance.model.Employee;
import com.internship.insurance.repository.EmployeeRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminController(EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/login")
    public boolean login(@RequestBody Employee employee) {
        Optional<Employee> employeeFromDb = Optional.ofNullable(employeeRepo.findByUsername(employee.getUsername()));

        return employeeFromDb.isPresent();
    }

    @RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        return () ->  new String(Base64.getDecoder()
                .decode(authToken)).split(":")[0];
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{id}")
    public Employee getOneEmployee(@PathVariable Long id) throws NotFoundException {
        Optional<Employee> employeeFromDb = employeeRepo.findById(id);

        if (employeeFromDb.isPresent())
            return employeeFromDb.get();
        else
            throw new NotFoundException("Employee not found.");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/add")
    public Employee addOneEmployee(@RequestBody Employee employee) {
        Employee newEmployee = new Employee();
        BeanUtils.copyProperties(employee, newEmployee);

        newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepo.save(newEmployee);
        return employee;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{id}")
    public Employee editOneEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee
    ) {
        employee.setId(id);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepo.save(employee);
        return employee;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{id}")
    public void deleteOneEmployee(@PathVariable Long id) {
        employeeRepo.deleteById(id);
    }
}
