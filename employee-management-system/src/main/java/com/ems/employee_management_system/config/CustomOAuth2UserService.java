package com.ems.employee_management_system.config;

import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // Load from Google
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        // Find user — check by username first, then by email (handle duplicates safely)
        User user = findOrCreateUser(email, name);

        // Assign role from DB
        String role = (user.getRole() != null) ? user.getRole().toUpperCase() : "EMPLOYEE";
        List<SimpleGrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_" + role));

        String userNameAttrKey = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttrKey);
    }

    private User findOrCreateUser(String email, String name) {
        // 1. Try by username (email used as username for Google users)
        Optional<User> byUsername = userRepository.findByUsername(email);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }

        // 2. Try by email — get first if multiple exist (cleanup duplicates)
        List<User> byEmail = userRepository.findByEmail(email);
        if (!byEmail.isEmpty()) {
            User existing = byEmail.get(0);

            // Delete duplicates if any
            if (byEmail.size() > 1) {
                for (int i = 1; i < byEmail.size(); i++) {
                    userRepository.delete(byEmail.get(i));
                }
            }

            // Make sure username is set to email
            if (existing.getUsername() == null || existing.getUsername().isBlank()) {
                existing.setUsername(email);
                userRepository.save(existing);
            }

            return existing;
        }

        // 3. Create new user
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setPassword("");
        newUser.setRole("EMPLOYEE");
        return userRepository.save(newUser);
    }
}
