package com.internship.insurance.rest;

import com.internship.insurance.dto.EmployeeDto;
import com.internship.insurance.model.Employee;
import com.internship.insurance.service.UserService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserRest {

    private final UserService employeeService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


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

    @GetMapping("/name/{username}")
    public ResponseEntity<EmployeeDto> getOne(@PathVariable String username) {
        Employee employee = employeeService.findByUsername(username);

        if (employee == null) {
            logger.error("Error: " + "Employee with username: " + username + ", not found!");
            return ResponseEntity.badRequest().build();
        }
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
        employeeService.update(employee);

        return ResponseEntity.ok(EmployeeDto.fromEmployee(employee));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
