/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Yves
 */
@Controller
public class HomeController {

    @Value(value = "${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public String redirectTo() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("appName", appName);
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
}
