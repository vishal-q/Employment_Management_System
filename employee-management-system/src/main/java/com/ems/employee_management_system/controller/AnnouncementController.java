package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Announcement;
import com.ems.employee_management_system.repository.AnnouncementRepository;
import com.ems.employee_management_system.service.NotificationService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired private AnnouncementRepository repo;
    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String list(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);
        model.addAttribute("announcements",
            isAdmin ? repo.findAllByOrderByCreatedAtDesc()
                    : repo.findByActiveTrueOrderByCreatedAtDesc());
        model.addAttribute("isAdmin", isAdmin);
        return "announcements";
    }

    @PostMapping("/post")
    public String post(@RequestParam String title,
                       @RequestParam String content,
                       @RequestParam String category,
                       Authentication auth,
                       RedirectAttributes ra) {
        Announcement a = new Announcement();
        a.setTitle(title);
        a.setContent(content);
        a.setCategory(category);
        a.setPostedBy(auth.getName());
        a.setCreatedAt(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        a.setActive(true);
        repo.save(a);

        notificationService.sendToAllEmployees(auth.getName(),
            "Announcement: " + title,
            content.length() > 100 ? content.substring(0, 100) + "..." : content,
            "INFO", "/announcements");

        ra.addFlashAttribute("success", "Announcement posted!");
        return "redirect:/announcements";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Announcement deleted.");
        return "redirect:/announcements";
    }
}
