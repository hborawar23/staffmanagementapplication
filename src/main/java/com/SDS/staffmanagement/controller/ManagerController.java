package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String dashboard()
    {
        return "Manager/manager_dashboard";
    }

    @GetMapping("/download_staff_profile")
    public String getAllStaff(Model model){
        model.addAttribute("title", "Manager - Download Staff Profile");
        List <User> allStaff= userService.getAllStaff();
        model.addAttribute("allStaff",allStaff);
        return "Manager/download_staff_profile";
    }

}


