//package com.keysoft.ecommerce.security;
//
//import com.keysoft.ecommerce.dto.RoleDTO;
//import com.keysoft.ecommerce.model.Role;
//import com.keysoft.ecommerce.model.User;
//import com.keysoft.ecommerce.repository.UserRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class CustomUserDetailService implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ModelMapper modelMapper;
//
//    private final Set<GrantedAuthority> authorities = new HashSet<>();
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username).orElse(null);
//        if (user == null) {
//            throw new UsernameNotFoundException("Not found");
//        }
//
//        return new CustomUserDetails(user);
//    }
//}
