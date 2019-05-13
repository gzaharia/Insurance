package com.internship.insurance.rest;

import com.internship.insurance.dto.EmployeeDto;
import com.internship.insurance.model.Employee;
import com.internship.insurance.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserRest {

    private final UserService employeeService;

    public UserRest(UserService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        List<Employee> employees = employeeService.getAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        employees.forEach(employee ->
                employeeDtos.add(EmployeeDto.fromEmployee(employee))
        );

        return ResponseEntity.ok(employeeDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getOne(@PathVariable Long id) throws NotFoundException {
        Employee employee = employeeService.findById(id);

        if (employee == null)
            throw new NotFoundException("Employee not found!");

        EmployeeDto employeeDto = EmployeeDto.fromEmployee(employee);

        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> addNew(@RequestBody Employee employee) {
        employeeService.register(employee);
        return ResponseEntity.ok(EmployeeDto.fromEmployee(employee));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<EmployeeDto> editEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee
    ) {
        Employee newEmployee = new Employee();
        BeanUtils.copyProperties(employee, newEmployee);
        newEmployee.setId(id);
        employeeService.register(employee);

        return ResponseEntity.ok(EmployeeDto.fromEmployee(newEmployee));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
