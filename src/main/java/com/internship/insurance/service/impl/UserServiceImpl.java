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
import java.util.Optional;
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
        Set<Role> newRoles = new HashSet<>();
        if (employee.getRoles() == null || employee.getRoles().isEmpty()) {
            Role newEmployeeRole = roleRepo.findByName("ROLE_MODERATOR");
            newRoles.add(newEmployeeRole);
            employee.setRoles(newRoles);
        } else {
            for (Role role : employee.getRoles())
                newRoles.add(roleRepo.findByName(role.getName()));
            employee.setRoles(newRoles);
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setStatus(Status.ACTIVE);

        return employeeRepo.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        Optional<Employee> employeeFromDb = employeeRepo.findById(employee.getId());
        Employee newEmployee = new Employee();

        if (employee.getPassword() == null && employeeFromDb.isPresent())
            newEmployee.setPassword(employeeFromDb.get().getPassword());
        else
            newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));

        newEmployee.setUserName(employee.getUserName());
        newEmployee.setStatus(employee.getStatus());
        newEmployee.setId(employee.getId());
        newEmployee.setFirstName(employee.getFirstName());
        newEmployee.setLastName(employee.getLastName());

        if (employee.getRoles() == null)
            newEmployee.setRoles(employeeFromDb.get().getRoles());
        else {
            Set<Role> newRoles = new HashSet<>();
            for (Role role : employee.getRoles())
                newRoles.add(roleRepo.findByName(role.getName()));
            newEmployee.setRoles(newRoles);
        }

        return employeeRepo.save(newEmployee);
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
