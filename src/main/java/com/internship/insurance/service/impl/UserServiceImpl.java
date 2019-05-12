package com.internship.insurance.service.impl;

import com.internship.insurance.model.Employee;
import com.internship.insurance.model.Role;
import com.internship.insurance.model.Status;
import com.internship.insurance.repository.EmployeeRepo;
import com.internship.insurance.repository.RoleRepo;
import com.internship.insurance.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final EmployeeRepo employeeRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(EmployeeRepo employeeRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Employee register(Employee employee) {
        Role newEmployeeRole = roleRepo.findByName("ROLE_MODERATOR");
        Set<Role> newEmployeeRoles = new HashSet<>();
        newEmployeeRoles.add(newEmployeeRole);

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setRoles(newEmployeeRoles);
        employee.setStatus(Status.ACTIVE);

        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee findByUsername(String username) {
        return employeeRepo.findByUserName(username);
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        employeeRepo.deleteById(id);
    }
}
