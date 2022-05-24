/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Notification;
import com.example.entities.User;
import com.example.responses.NotificationResponse;
import com.example.services.NotificationService;
import com.example.services.UserService;
import com.example.util.CustomUserDetail;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Yves
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private static final String NOTIF_LIST = "notif/list";

    @Autowired
    private NotificationService ns;

    @Autowired
    private UserService us;

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal CustomUserDetail principal, Model model) {
        User u = us.one(principal.getUsername()).orElse(null);
        model.addAttribute("notifications",
                ns.all(u.getBenevole()).stream().map(NotificationResponse::new).collect(Collectors.toList()));
        return NOTIF_LIST;
    }

    @GetMapping("/{id}/delete")
    public String destroy(Model model, @PathVariable("id") Long id) {
        Notification n = ns.one(id).orElse(null);
        if (n != null) {
            ns.delete(id);
            return "redirect:/notifications/index";
        }
        return NOTIF_LIST;
    }
}
