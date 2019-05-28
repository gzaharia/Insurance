package com.internship.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.internship.insurance.model.Employee;
import com.internship.insurance.model.Role;
import com.internship.insurance.model.Status;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {

    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
    private String status;

    public Employee toEmployee() {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setUserName(userName);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setRoles(roles);
        employee.setStatus(Status.valueOf(status));

        return employee;
    }

    public static EmployeeDto fromEmployee(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        employeeDto.setUserName(employee.getUserName());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setRoles(employee.getRoles());
        employeeDto.setStatus(employee.getStatus().name());

        return employeeDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
