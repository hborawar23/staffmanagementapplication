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
    public String dashboard(Model model, Principal principal)
    {
        String name = principal.getName();
        System.out.println("USERNAME" + name);

        User user = userRepository.getUserByUserName(name);
        System.out.println("USER" + user);
        //get the user using username(Email)
        return "Staff/staff_dashboard";
    }
}
