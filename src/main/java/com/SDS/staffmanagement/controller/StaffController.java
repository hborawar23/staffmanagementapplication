package com.SDS.staffmanagement.controller;

import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private UserRepository userRepository;
    @RequestMapping("/")
    public String dashboard(Model model)
    {
        model.addAttribute("title","Staff Dashboard");
        return "Staff/staff_dashboard";
    }
    @RequestMapping("/calendar")
    public String openCalendar(Model model){
        model.addAttribute("title","Calendar");
        return "calendar";
    }




}
