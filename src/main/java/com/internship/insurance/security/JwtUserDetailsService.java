package com.internship.insurance.security;

import com.internship.insurance.model.Employee;
import com.internship.insurance.security.jwt.JwtUserFactory;
import com.internship.insurance.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService employeeService;

    public JwtUserDetailsService(UserService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByUsername(username);

        if (employee == null)
            throw new UsernameNotFoundException("User with username: " + username + ", not found!");

        return JwtUserFactory.create(employee);
    }
}
