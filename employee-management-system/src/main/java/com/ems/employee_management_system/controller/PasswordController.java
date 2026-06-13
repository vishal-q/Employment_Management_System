package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.UserRepository;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/change-password")
public class PasswordController {

    @Autowired private UserRepository  userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthHelper      authHelper;

    @GetMapping
    public String page(Model model) {
        return "change-password";
    }

    @PostMapping
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes ra) {

        User user = authHelper.getCurrentUser(auth);
        if (user == null) return "redirect:/login";

        // Validate current password
        if (user.getPassword() == null || user.getPassword().isBlank()
            || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            ra.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/change-password";
        }

        // Confirm new passwords match
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "New passwords do not match.");
            return "redirect:/change-password";
        }

        if (newPassword.length() < 6) {
            ra.addFlashAttribute("error", "Password must be at least 6 characters.");
            return "redirect:/change-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        ra.addFlashAttribute("success", "Password changed successfully! Please login again.");
        return "redirect:/logout";
    }
}
