package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Holiday;
import com.ems.employee_management_system.repository.HolidayRepository;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/holidays")
public class HolidayController {

    @Autowired private HolidayRepository holidayRepo;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String holidayPage(Model model, Authentication auth) {
        String today = LocalDate.now().toString();
        model.addAttribute("holidays",         holidayRepo.findAllByOrderByDateAsc());
        model.addAttribute("upcomingHolidays", holidayRepo.findByDateGreaterThanEqualOrderByDateAsc(today));
        model.addAttribute("isAdmin",          authHelper.isAdmin(auth));
        model.addAttribute("today",            today);
        return "holidays";
    }

    @PostMapping("/add")
    public String addHoliday(@RequestParam String name,
                              @RequestParam String date,
                              @RequestParam String type,
                              @RequestParam(required = false) String description,
                              Authentication auth,
                              RedirectAttributes ra) {
        Holiday h = new Holiday();
        h.setName(name);
        h.setDate(date);
        h.setType(type);
        h.setDescription(description);
        h.setCreatedBy(auth.getName());
        holidayRepo.save(h);
        ra.addFlashAttribute("success", "Holiday added: " + name);
        return "redirect:/holidays";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        holidayRepo.deleteById(id);
        ra.addFlashAttribute("success", "Holiday deleted.");
        return "redirect:/holidays";
    }
}
