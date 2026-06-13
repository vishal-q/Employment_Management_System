package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Message;
import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.MessageRepository;
import com.ems.employee_management_system.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired private MessageRepository messageRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping
    public String inbox(Model model, Authentication auth) {
        User currentUser = authHelper.getCurrentUser(auth);
        if (currentUser == null) return "redirect:/dashboard";

        boolean isAdmin = authHelper.isAdmin(auth);

        List<Message> inbox;
        List<Message> sent;

        if (isAdmin) {
            inbox = messageRepo.findByFromRoleOrderBySentAtDesc("EMPLOYEE");
            sent  = messageRepo.findByFromUserIdOrderBySentAtDesc(currentUser.getId());
        } else {
            inbox = messageRepo.findByToUserIdOrderBySentAtDesc(currentUser.getId());
            sent  = messageRepo.findByFromUserIdOrderBySentAtDesc(currentUser.getId());
        }

        // Mark as read
        inbox.forEach(m -> {
            if (!m.isReadByRecipient()) { m.setReadByRecipient(true); messageRepo.save(m); }
        });

        if (isAdmin) {
            List<User> employees = new ArrayList<>();
            userRepo.findAll().forEach(u -> {
                if ("EMPLOYEE".equals(u.getRole())) employees.add(u);
            });
            model.addAttribute("employees", employees);
        }

        model.addAttribute("inbox",       inbox);
        model.addAttribute("sent",        sent);
        model.addAttribute("isAdmin",     isAdmin);
        model.addAttribute("currentUser", currentUser);
        return "messages";
    }

    @PostMapping("/send")
    public String send(@RequestParam(required = false) String toUserId,
                       @RequestParam String subject,
                       @RequestParam String body,
                       Authentication auth,
                       RedirectAttributes ra) {
        User sender = authHelper.getCurrentUser(auth);
        if (sender == null) { ra.addFlashAttribute("error", "Session error."); return "redirect:/messages"; }

        boolean isAdmin = authHelper.isAdmin(auth);

        Message msg = new Message();
        msg.setFromUserId(sender.getId());
        msg.setFromName(sender.getName() != null ? sender.getName() : sender.getUsername());
        msg.setFromRole(isAdmin ? "ADMIN" : "EMPLOYEE");
        msg.setSubject(subject);
        msg.setBody(body);
        msg.setSentAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        if (isAdmin && toUserId != null && !toUserId.isBlank()) {
            Optional<User> recipient = userRepo.findById(toUserId);
            recipient.ifPresent(r -> {
                msg.setToUserId(r.getId());
                msg.setToName(r.getName() != null ? r.getName() : r.getUsername());
            });
            messageRepo.save(msg);
            notificationService.sendToUser(toUserId, msg.getFromName(),
                "New Message: " + subject,
                body.length() > 80 ? body.substring(0, 80) + "..." : body,
                "MESSAGE", "/messages");
        } else if (!isAdmin) {
            msg.setToName("Admin");
            messageRepo.save(msg);
            notificationService.sendToAdmins(msg.getFromName(),
                "Message from " + msg.getFromName() + ": " + subject,
                body.length() > 80 ? body.substring(0, 80) + "..." : body,
                "MESSAGE", "/messages");
        }

        ra.addFlashAttribute("success", "Message sent!");
        return "redirect:/messages";
    }
}
