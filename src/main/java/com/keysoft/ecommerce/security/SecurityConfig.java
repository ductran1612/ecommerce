package com.keysoft.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig{
    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/shop/**", "/product/product/{code}",  "/product/{code}", "/admin/category/api/navbar",
                        "/images/**", "/product/api/{code}", "/shop/register", "/customer/save").permitAll()
                .requestMatchers("/resources/assets/**").permitAll()
                .requestMatchers("/admin/home").hasAnyRole("CASHIER", "WAREHOUSE")
                .requestMatchers("/admin/group/**", "/admin/user/**", "/admin/customer/**").hasRole("ADMIN")
                .requestMatchers("/admin/category/**", "/admin/customer/**", "/admin/supplier/**").hasRole("MANAGER")
                .requestMatchers("/admin/transaction/**", "/admin/product/**").hasRole("CASHIER")
                .requestMatchers("/admin/stock/**").hasRole("WAREHOUSE")
                .requestMatchers("/shop/**", "/customer/**").hasRole("CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/shop/login").permitAll()
                .successHandler(customSuccessHandler)
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutUrl("/shop/logout")
                .logoutSuccessUrl("/shop/home");
        http.exceptionHandling().accessDeniedPage("/shop/login?accessDenied");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }
}
