package com.keysoft.ecommerce.security;

import com.keysoft.ecommerce.jwt.JwtAuthenticationFilter;
import com.keysoft.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private CustomSuccessHandler customSuccessHandler;
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth ->
                        auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/resources/assets/**").permitAll()
                                .requestMatchers("/admin/home").hasAnyRole("CASHIER", "WAREHOUSE")
                                .requestMatchers("/admin/group/**", "/admin/user/**", "/admin/customer/**").hasRole("ADMIN")
                                .requestMatchers("/admin/category/**", "/admin/customer/**").hasRole("MANAGER")
                                .requestMatchers("/admin/transaction/**", "/admin/product/**").hasRole("CASHIER")
                                .requestMatchers("/admin/stock/**").hasRole("WAREHOUSE")
                                .requestMatchers("/admin/rating/create", "/customer/**", "/admin/product/list").hasRole("CUSTOMER")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login").permitAll()
//                                .successHandler(customSuccessHandler)
//                                .failureUrl("/login?error=true")
//                )
//                .logout(logout ->
//                        logout
//                                .logoutUrl("/logout")
//                                .logoutSuccessUrl("/home")
//                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.accessDeniedPage("/login?accessDenied")
//                );
        return http.build();
    }



}
