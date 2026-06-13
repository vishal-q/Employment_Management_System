package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.service.NotificationService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String notifications(Model model, Authentication auth) {
        User user = authHelper.getCurrentUser(auth);
        if (user == null) return "redirect:/dashboard";

        String role = authHelper.isAdmin(auth) ? "ADMIN" : "EMPLOYEE";

        model.addAttribute("notifications",
            notificationService.getForUser(user.getId(), role));
        model.addAttribute("unreadCount",
            notificationService.countUnread(user.getId(), role));
        return "notifications";
    }

    @PostMapping("/markRead")
    public String markRead(Authentication auth, RedirectAttributes ra) {
        User user = authHelper.getCurrentUser(auth);
        if (user != null) notificationService.markAllRead(user.getId());
        ra.addFlashAttribute("success", "All notifications marked as read.");
        return "redirect:/notifications";
    }
}
