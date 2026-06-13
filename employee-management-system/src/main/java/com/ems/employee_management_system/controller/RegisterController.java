package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(defaultValue = "EMPLOYEE") String role,
                               Model model) {

        // Username already exists check
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            model.addAttribute("error", "Username already taken. Please choose another.");
            return "register";
        }

        // Validate role
        if (!role.equals("ADMIN") && !role.equals("EMPLOYEE")) {
            role = "EMPLOYEE";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);

        return "redirect:/login?registered=true";
    }
}
