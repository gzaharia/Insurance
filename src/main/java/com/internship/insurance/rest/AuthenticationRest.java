package com.internship.insurance.rest;

import com.internship.insurance.dto.AuthenticationRequestDto;
import com.internship.insurance.model.Employee;
import com.internship.insurance.security.jwt.JwtTokenProvider;
import com.internship.insurance.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin
public class AuthenticationRest {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService employeeService;

    public AuthenticationRest(AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider,
                              UserService employeeService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            username, requestDto.getPassword()
            ));

            Employee employee = employeeService.findByUsername(username);

            if (employee == null)
                throw new UsernameNotFoundException("User with username: " + username + ", not found!");

            String token = jwtTokenProvider.createToken(username, employee.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password!");
        }
    }
}
