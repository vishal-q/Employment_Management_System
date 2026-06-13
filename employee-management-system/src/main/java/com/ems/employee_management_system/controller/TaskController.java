package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.Task;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.repository.TaskRepository;
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
@RequestMapping("/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepo;
    @Autowired private EmployeeRepository empRepo;
    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String tasksPage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);

        if (isAdmin) {
            model.addAttribute("tasks",           taskRepo.findAllByOrderByAssignedOnDesc());
            model.addAttribute("pendingCount",    taskRepo.countByStatus("Pending"));
            model.addAttribute("inProgressCount", taskRepo.countByStatus("In Progress"));
            model.addAttribute("employees",       empRepo.findAll());
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                model.addAttribute("tasks",           taskRepo.findByEmployeeIdOrderByAssignedOnDesc(emp.getId()));
                model.addAttribute("employee",        emp);
                model.addAttribute("pendingCount",    taskRepo.countByEmployeeIdAndStatus(emp.getId(), "Pending"));
                model.addAttribute("inProgressCount", taskRepo.countByEmployeeIdAndStatus(emp.getId(), "In Progress"));
                model.addAttribute("completedCount",  taskRepo.countByEmployeeIdAndStatus(emp.getId(), "Completed"));
            } else {
                model.addAttribute("noProfile", true);
                model.addAttribute("tasks", List.of());
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "tasks";
    }

    @PostMapping("/assign")
    public String assign(@RequestParam String employeeId,
                         @RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String priority,
                         @RequestParam String dueDate,
                         Authentication auth,
                         RedirectAttributes ra) {
        Employee emp = empRepo.findById(employeeId).orElse(null);
        if (emp == null) { ra.addFlashAttribute("error", "Employee not found."); return "redirect:/tasks"; }

        Task t = new Task();
        t.setEmployeeId(emp.getId());
        t.setEmployeeName(emp.getName());
        t.setDepartment(emp.getDepartment());
        t.setAssignedBy(auth.getName());
        t.setTitle(title);
        t.setDescription(description);
        t.setPriority(priority);
        t.setDueDate(dueDate);
        t.setStatus("Pending");
        t.setAssignedOn(LocalDate.now().toString());
        taskRepo.save(t);

        // Notify employee via userId
        String notifyId = emp.getUserId() != null ? emp.getUserId() : emp.getId();
        notificationService.sendToUser(notifyId, auth.getName(),
            "New Task: " + title,
            "Priority: " + priority + " | Due: " + dueDate,
            "TASK", "/tasks");

        ra.addFlashAttribute("success", "Task assigned to " + emp.getName() + "!");
        return "redirect:/tasks";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable String id,
                               @RequestParam String status,
                               @RequestParam(required = false) String employeeNote,
                               Authentication auth,
                               RedirectAttributes ra) {
        Optional<Task> opt = taskRepo.findById(id);
        opt.ifPresent(t -> {
            t.setStatus(status);
            if (employeeNote != null && !employeeNote.isBlank()) t.setEmployeeNote(employeeNote);
            if ("Completed".equals(status)) t.setCompletedOn(LocalDate.now().toString());
            taskRepo.save(t);
            notificationService.sendToAdmins(t.getEmployeeName(),
                "Task Update: " + t.getTitle(),
                t.getEmployeeName() + " marked '" + t.getTitle() + "' as " + status,
                "TASK", "/tasks");
        });
        ra.addFlashAttribute("success", "Task updated!");
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        taskRepo.deleteById(id);
        ra.addFlashAttribute("success", "Task deleted.");
        return "redirect:/tasks";
    }
}
