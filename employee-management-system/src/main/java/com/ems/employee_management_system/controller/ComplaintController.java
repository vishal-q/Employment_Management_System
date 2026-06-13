package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Complaint;
import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.repository.ComplaintRepository;
import com.ems.employee_management_system.service.NotificationService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired private ComplaintRepository complaintRepo;
    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String complaintsPage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);

        if (isAdmin) {
            model.addAttribute("complaints", complaintRepo.findAllByOrderBySubmittedOnDesc());
            model.addAttribute("openCount",  complaintRepo.countByStatus("Open"));
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                model.addAttribute("complaints", complaintRepo.findByEmployeeIdOrderBySubmittedOnDesc(emp.getId()));
                model.addAttribute("employee",   emp);
            } else {
                model.addAttribute("noProfile", true);
                model.addAttribute("complaints", List.of());
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "complaints";
    }

    @PostMapping("/submit")
    public String submit(@RequestParam String category,
                         @RequestParam String subject,
                         @RequestParam String description,
                         @RequestParam(defaultValue = "false") boolean anonymous,
                         Authentication auth,
                         RedirectAttributes ra) {
        Employee emp = authHelper.getLinkedEmployee(auth);
        if (emp == null) { ra.addFlashAttribute("error", "Profile not linked."); return "redirect:/complaints"; }

        Complaint c = new Complaint();
        c.setEmployeeId(emp.getId());
        c.setEmployeeName(anonymous ? "Anonymous" : emp.getName());
        c.setDepartment(emp.getDepartment());
        c.setCategory(category);
        c.setSubject(subject);
        c.setDescription(description);
        c.setAnonymous(anonymous);
        c.setSubmittedOn(LocalDate.now().toString());
        complaintRepo.save(c);

        notificationService.sendToAdmins(
            anonymous ? "Anonymous" : emp.getName(),
            "New Complaint: " + subject,
            "Category: " + category,
            "WARNING", "/complaints");

        ra.addFlashAttribute("success", "Complaint submitted.");
        return "redirect:/complaints";
    }

    @PostMapping("/respond/{id}")
    public String respond(@PathVariable String id,
                          @RequestParam String adminResponse,
                          @RequestParam String status,
                          RedirectAttributes ra) {
        Optional<Complaint> opt = complaintRepo.findById(id);
        opt.ifPresent(c -> {
            c.setAdminResponse(adminResponse);
            c.setStatus(status);
            if ("Resolved".equals(status) || "Closed".equals(status))
                c.setResolvedOn(LocalDate.now().toString());
            complaintRepo.save(c);

            if (!c.isAnonymous()) {
                notificationService.sendToUser(c.getEmployeeId(), "Admin",
                    "Complaint Update: " + c.getSubject(),
                    "Status: " + status + ". Response: " + adminResponse,
                    "INFO", "/complaints");
            }
        });
        ra.addFlashAttribute("success", "Response submitted.");
        return "redirect:/complaints";
    }
}
