package com.internship.insurance.config;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // Main Api mappings available for everyone
                .antMatchers("/api").permitAll()

                // Login form
                .antMatchers("/api/auth/login").permitAll()

                // User - only for ADMIN
                .antMatchers("/api/admin/users").hasRole("ADMIN")
                .antMatchers("/api/admin/users/**").hasRole("ADMIN")

                // Create, Update or Delete categories - only for ADMIN
                .antMatchers("/api/categories/add").hasRole("ADMIN")
                .antMatchers("/api/categories/edit/**").hasRole("ADMIN")
                .antMatchers("/api/categories/delete/**").hasRole("ADMIN")

                // Create, Update or Delete properties - only for ADMIN
                .antMatchers("/api/properties/add").hasRole("ADMIN")
                .antMatchers("/api/properties/edit/**").hasRole("ADMIN")
                .antMatchers("/api/properties/delete/**").hasRole("ADMIN")

                // Admin dashboard available for MODERATOR
                .antMatchers("/api/admin/**").hasRole("MODERATOR")

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/api/categories")
                .antMatchers("/api/properties");
    }
}
