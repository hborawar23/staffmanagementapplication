package com.SDS.staffmanagement.controller;

import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/hr")
public class HrController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @RequestMapping("/")
    public String view_dashboard(Model model)
    {
        model.addAttribute("title","Admin Dashboard");

        return "HR/hr_dashboard";
    }
    @RequestMapping("/modify_staff")
    public String openCalendar(Model model){
        model.addAttribute("title","Employee Details");
        return "/modify_staff";
    }

}
