package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.security.CustomUserDetails;
import com.keysoft.ecommerce.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    private final Set<GrantedAuthority> authorities = new HashSet<>();


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.findByUsername(username);
        CustomUserDetails customUserDetail;
        if (user == null) {
            throw new UsernameNotFoundException("Not found");
        }

        for (RoleDTO role : user.getGroup().getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        customUserDetail = new CustomUserDetail();
        customUserDetail.setUser(user);
        customUserDetail.setAuthorities(authorities);

        return customUserDetail;
    }

    public void updateLoginAttempts(String username, boolean success) {
        UserDTO user = userService.findByUsername(username);
        if (user != null) {
            if (success) {
                user.resetLoginAttempts();
            } else {
                user.incrementLoginAttempts();
                if (user.getLoginAttempts() >= 5) {
                    user.setEnable(false);
                }
                System.out.println(user.getLoginAttempts());
            }
        }

    }
}
