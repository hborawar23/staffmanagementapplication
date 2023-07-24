package com.staffmanagement.controller;

import com.staffmanagement.entities.User;
import com.staffmanagement.services.EmailService;
import com.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class HomeController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Staff Management System");
        return "home";
    }
    @RequestMapping("/signup")
    public String about(Model model) {
        model.addAttribute("title", "Register - Staff Management System");
        model.addAttribute("user", new User());
        return "signup";
    }
    @RequestMapping("/signin")
    public String login(Model model){
        model.addAttribute("title","Login-Staff Management System");
        return "login_page";
    }
    @RequestMapping(value = "/confirm-account")
    public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken) {
        if (emailService.confirmUser(confirmationToken)) {
            model.addAttribute("success", "your email has been verified. you can now login");
            return "verify-success.html";
        }
        model.addAttribute("notSuccess", "your email is not verified");
         return "verify-fail.html";
    }
}

