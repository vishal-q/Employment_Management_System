package com.ems.employee_management_system.service;

import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.UserRepository;
import com.ems.employee_management_system.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try by username first
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return new CustomUserDetails(byUsername.get());
        }
        // Try by email (Google OAuth users)
        List<User> byEmail = userRepository.findByEmail(username);
        if (!byEmail.isEmpty()) {
            return new CustomUserDetails(byEmail.get(0));
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
