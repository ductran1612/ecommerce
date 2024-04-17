package com.keysoft.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.jwt.JwtService;
import com.keysoft.ecommerce.model.Customer;
import com.keysoft.ecommerce.model.Role;
import com.keysoft.ecommerce.model.User;
import com.keysoft.ecommerce.repository.CustomerRepository;
import com.keysoft.ecommerce.repository.GroupRepository;
import com.keysoft.ecommerce.repository.UserRepository;
import com.keysoft.ecommerce.token.Token;
import com.keysoft.ecommerce.token.TokenRepository;
import com.keysoft.ecommerce.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper modelMapper;

    public AuthenticationResponse register(UserDTO userDTO) {
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .password(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(10)))
                .build();
        user.setGroup(groupRepository.findByCode("client").orElse(null));
        user.setEnable(true);
        Customer customer = modelMapper.map(user, Customer.class);
        customerRepository.save(customer);
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(UserDTO userDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        Set<String> resultsRole = new HashSet<>();
        for(Role role : user.getGroup().getRoles()) {
            resultsRole.add(role.getCode());
        }
        claims.put("roles", resultsRole);
        String jwtToken = jwtService.generateToken(claims, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            User user = this.userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
