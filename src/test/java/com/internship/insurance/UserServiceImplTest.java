package com.internship.insurance;

import com.internship.insurance.model.Employee;
import com.internship.insurance.repository.EmployeeRepo;
import com.internship.insurance.service.UserService;
import com.internship.insurance.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private EmployeeRepo employeeRepo;
    private UserService userService;
    private Employee employee;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(employeeRepo, null, null);

        employee = new Employee("admin3", "Ion", "Chirosca", "admin3", null);
        employee.setId(1l);

        when(employeeRepo.findByUserName(employee.getUserName())).thenReturn(employee);
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
    }

    @Test
    public void testFindEmployeeByUserName() {
        Employee employeeByName = userService.findByUsername(employee.getUserName());
        verify(employeeRepo).findByUserName(employee.getUserName());
        assertThat(employeeByName).isEqualTo(employee);
    }

    @Test
    public void testFindEmployeeById() {
        Employee employeById = userService.findById(employee.getId());
        verify(employeeRepo).findById(employee.getId());
        assertThat(employeById).isEqualTo(employee);
    }

}
