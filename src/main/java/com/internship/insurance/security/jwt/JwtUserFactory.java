package com.internship.insurance.security.jwt;

import com.internship.insurance.model.Employee;
import com.internship.insurance.model.Role;
import com.internship.insurance.model.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Employee employee) {
        return new JwtUser(
                employee.getId(),
                employee.getUserName(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPassword(),
                employee.getStatus().equals(Status.ACTIVE),
                employee.getUpdated(),
                mapToGrantedAuthorities(new HashSet<>(employee.getRoles()))
        );
    }

    public static Set<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
