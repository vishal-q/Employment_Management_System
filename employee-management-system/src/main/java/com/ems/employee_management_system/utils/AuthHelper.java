package com.ems.employee_management_system.utils;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Shared utility for resolving current user / employee from Authentication.
 * Handles both form-login (UserDetails) and Google OAuth2 (OAuth2User).
 * Safe against duplicate email entries in MongoDB.
 */
@Component
public class AuthHelper {

    private final UserRepository userRepo;
    private final EmployeeRepository empRepo;

    public AuthHelper(UserRepository userRepo, EmployeeRepository empRepo) {
        this.userRepo = userRepo;
        this.empRepo  = empRepo;
    }

    /** Returns the User entity for the currently authenticated user. */
    public User getCurrentUser(Authentication auth) {
        if (auth == null) return null;
        Object p = auth.getPrincipal();

        if (p instanceof UserDetails ud) {
            return userRepo.findByUsername(ud.getUsername()).orElse(null);
        }

        if (p instanceof OAuth2User o) {
            String email = o.getAttribute("email");
            if (email == null) return null;
            // findByUsername first (most reliable — email stored as username for Google users)
            Optional<User> byUsername = userRepo.findByUsername(email);
            if (byUsername.isPresent()) return byUsername.get();
            // Fallback: findByEmail (returns list — take first safely)
            List<User> byEmail = userRepo.findByEmail(email);
            return byEmail.isEmpty() ? null : byEmail.get(0);
        }
        return null;
    }

    /** Returns the current User's DB id. */
    public String getCurrentUserId(Authentication auth) {
        User u = getCurrentUser(auth);
        return u != null ? u.getId() : null;
    }

    /** Returns the Employee linked to the current user (via userId field). */
    public Employee getLinkedEmployee(Authentication auth) {
        String userId = getCurrentUserId(auth);
        if (userId == null) return null;
        return empRepo.findByUserId(userId).orElse(null);
    }

    /** Returns true if the current user has ROLE_ADMIN. */
    public boolean isAdmin(Authentication auth) {
        if (auth == null) return false;
        return auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
