package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.PerformanceReview;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.repository.PerformanceRepository;
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

@Controller
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired private PerformanceRepository perfRepo;
    @Autowired private EmployeeRepository    empRepo;
    @Autowired private NotificationService   notificationService;
    @Autowired private AuthHelper            authHelper;

    @GetMapping
    public String performancePage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);

        if (isAdmin) {
            model.addAttribute("reviews",   perfRepo.findAllByOrderByCreatedOnDesc());
            model.addAttribute("employees", empRepo.findAll());
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                model.addAttribute("reviews",  perfRepo.findByEmployeeIdOrderByCreatedOnDesc(emp.getId()));
                model.addAttribute("employee", emp);
            } else {
                model.addAttribute("noProfile", true);
                model.addAttribute("reviews", List.of());
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "performance";
    }

    @PostMapping("/add")
    public String addReview(@RequestParam String employeeId,
                            @RequestParam String reviewPeriod,
                            @RequestParam String reviewType,
                            @RequestParam int technicalSkills,
                            @RequestParam int communication,
                            @RequestParam int teamwork,
                            @RequestParam int punctuality,
                            @RequestParam int productivity,
                            @RequestParam(required = false) String strengths,
                            @RequestParam(required = false) String improvements,
                            @RequestParam(required = false) String comments,
                            @RequestParam(required = false) String goal,
                            Authentication auth,
                            RedirectAttributes ra) {

        Employee emp = empRepo.findById(employeeId).orElse(null);
        if (emp == null) { ra.addFlashAttribute("error", "Employee not found."); return "redirect:/performance"; }

        double overall = (technicalSkills + communication + teamwork + punctuality + productivity) / 5.0;

        PerformanceReview review = new PerformanceReview();
        review.setEmployeeId(employeeId);
        review.setEmployeeName(emp.getName());
        review.setDepartment(emp.getDepartment());
        review.setReviewedBy(auth.getName());
        review.setReviewPeriod(reviewPeriod);
        review.setReviewType(reviewType);
        review.setTechnicalSkills(technicalSkills);
        review.setCommunication(communication);
        review.setTeamwork(teamwork);
        review.setPunctuality(punctuality);
        review.setProductivity(productivity);
        review.setOverallRating(Math.round(overall * 10.0) / 10.0);
        review.setStrengths(strengths);
        review.setImprovements(improvements);
        review.setComments(comments);
        review.setGoal(goal);
        review.setStatus("Shared");
        review.setCreatedOn(LocalDate.now().toString());
        perfRepo.save(review);

        // Notify employee
        if (emp.getUserId() != null) {
            notificationService.sendToUser(emp.getUserId(), "HR",
                "Performance Review - " + reviewPeriod,
                "Your performance review for " + reviewPeriod + " has been submitted. Overall: " + review.getOverallRating() + "/5",
                "INFO", "/performance");
        }

        ra.addFlashAttribute("success", "Review submitted for " + emp.getName());
        return "redirect:/performance";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        perfRepo.deleteById(id);
        ra.addFlashAttribute("success", "Review deleted.");
        return "redirect:/performance";
    }
}
