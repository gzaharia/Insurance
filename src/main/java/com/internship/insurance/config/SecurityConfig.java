package com.internship.insurance.config;

import com.internship.insurance.model.Roles;
import com.internship.insurance.security.jwt.JwtConfigurer;
import com.internship.insurance.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("https://ng-insurance.herokuapp.com"));
//        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // Main Api mappings available for everyone
                .antMatchers("/api").permitAll()

                // Login form
                .antMatchers("/api/auth/login").permitAll()

                // User - only for ADMIN / MODERATOR
                .antMatchers("/api/admin/users/name/**").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/admin/users/edit/**").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/admin/users").hasRole(Roles.ADMIN.name())
                .antMatchers("/api/admin/users/**").hasRole(Roles.ADMIN.name())

                // Create, Update or Delete categories - only for ADMIN
                .antMatchers("/api/categories/add").hasRole(Roles.ADMIN.name())
                .antMatchers("/api/categories/edit/**").hasRole(Roles.ADMIN.name())
                .antMatchers("/api/categories/delete/**").hasRole(Roles.ADMIN.name())

                // Create, Update or Delete properties - only for ADMIN
                .antMatchers("/api/properties/add").hasRole(Roles.ADMIN.name())
                .antMatchers("/api/properties/edit/**").hasRole(Roles.ADMIN.name())
                .antMatchers("/api/properties/delete/**").hasRole(Roles.ADMIN.name())

                // Orders - MODERATOR / ADMIN
                .antMatchers("/api/orders").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/orders/pending").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/orders/approved").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/orders/declined").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/orders/{id}").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())
                .antMatchers("/api/orders/edit/status/{id}").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())

                // Input types - MODERATOR / ADMIN
                .antMatchers("/api/inputTypes").hasAnyRole(Roles.ADMIN.name(), Roles.MODERATOR.name())

                // Admin dashboard available for MODERATOR
                .antMatchers("/api/admin/**").hasAnyRole(Roles.MODERATOR.name(), Roles.ADMIN.name())

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/api/categories")
                .antMatchers("/api/properties/all")
                .antMatchers("/api/insurances")
                .antMatchers("/api/insurances/**")
                .antMatchers("/api/orders/price")
                .antMatchers("/api/orders/add");
//        web
//                .ignoring().anyRequest();
    }
}
