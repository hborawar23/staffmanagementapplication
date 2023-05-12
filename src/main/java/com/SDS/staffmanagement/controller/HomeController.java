package com.SDS.staffmanagement.controller;

import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.services.EmailService;
import com.SDS.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private EmailService emailService;

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

    //To get the login page
    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("title","Login-Staff Management System");
        return "login";
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

