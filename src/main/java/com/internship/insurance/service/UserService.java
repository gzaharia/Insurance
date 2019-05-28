package com.internship.insurance.service;

import com.internship.insurance.model.Employee;

import java.util.List;

public interface UserService {

    Employee register(Employee employee);

    Employee update(Employee employee);

    List<Employee> getAll();

    Employee findByUsername(String username);

    Employee findById(Long id);

    void delete(Long id);
}
