package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.WorkFromHomeRequest;
import com.ems.employee_management_system.repository.WorkFromHomeRepository;
import com.ems.employee_management_system.service.NotificationService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/wfh")
public class WorkFromHomeController {

    @Autowired private WorkFromHomeRepository wfhRepo;
    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String wfhPage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);

        if (isAdmin) {
            model.addAttribute("wfhList",      wfhRepo.findAllByOrderByAppliedOnDesc());
            model.addAttribute("pendingCount", wfhRepo.countByStatus("Pending"));
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                model.addAttribute("wfhList",  wfhRepo.findByEmployeeIdOrderByAppliedOnDesc(emp.getId()));
                model.addAttribute("employee", emp);
            } else {
                model.addAttribute("noProfile", true);
                model.addAttribute("wfhList",   List.of());
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "wfh";
    }

    @PostMapping("/apply")
    public String apply(@RequestParam String fromDate,
                        @RequestParam String toDate,
                        @RequestParam String reason,
                        Authentication auth,
                        RedirectAttributes ra) {
        Employee emp = authHelper.getLinkedEmployee(auth);
        if (emp == null) { ra.addFlashAttribute("error", "Profile not linked. Contact Admin."); return "redirect:/wfh"; }

        WorkFromHomeRequest w = new WorkFromHomeRequest();
        w.setEmployeeId(emp.getId());
        w.setEmployeeName(emp.getName());
        w.setDepartment(emp.getDepartment());
        w.setFromDate(fromDate);
        w.setToDate(toDate);
        w.setReason(reason);
        w.setAppliedOn(LocalDate.now().toString());
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int days = (int) ChronoUnit.DAYS.between(
                LocalDate.parse(fromDate, f), LocalDate.parse(toDate, f)) + 1;
            w.setTotalDays(days);
        } catch (Exception e) { w.setTotalDays(1); }

        wfhRepo.save(w);
        notificationService.sendToAdmins(emp.getName(),
            "WFH Request from " + emp.getName(),
            emp.getName() + " applied for WFH (" + fromDate + " to " + toDate + ")",
            "WFH", "/wfh");

        ra.addFlashAttribute("success", "WFH request submitted!");
        return "redirect:/wfh";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable String id,
                          @RequestParam(required = false) String adminRemarks,
                          RedirectAttributes ra) {
        Optional<WorkFromHomeRequest> opt = wfhRepo.findById(id);
        opt.ifPresent(w -> {
            w.setStatus("Approved");
            w.setAdminRemarks(adminRemarks);
            wfhRepo.save(w);
            notificationService.sendToUser(w.getEmployeeId(), "Admin",
                "WFH Approved!", "Your WFH (" + w.getFromDate() + " to " + w.getToDate() + ") is approved.",
                "SUCCESS", "/wfh");
        });
        ra.addFlashAttribute("success", "WFH approved.");
        return "redirect:/wfh";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable String id,
                         @RequestParam(required = false) String adminRemarks,
                         RedirectAttributes ra) {
        Optional<WorkFromHomeRequest> opt = wfhRepo.findById(id);
        opt.ifPresent(w -> {
            w.setStatus("Rejected");
            w.setAdminRemarks(adminRemarks);
            wfhRepo.save(w);
            notificationService.sendToUser(w.getEmployeeId(), "Admin",
                "WFH Rejected", "Your WFH request was rejected." +
                (adminRemarks != null ? " Reason: " + adminRemarks : ""),
                "WARNING", "/wfh");
        });
        ra.addFlashAttribute("success", "WFH rejected.");
        return "redirect:/wfh";
    }
}
