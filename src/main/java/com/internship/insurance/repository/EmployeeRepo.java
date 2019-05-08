package com.internship.insurance.repository;

import com.internship.insurance.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    Employee findByUsername(String username);
}
