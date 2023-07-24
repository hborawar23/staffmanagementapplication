package com.staffmanagement.controller;
import com.staffmanagement.entities.Manager;
import com.staffmanagement.entities.User;
import com.staffmanagement.services.EmailService;
import com.staffmanagement.services.ManagerService;
import com.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class Register_Controller {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ManagerService managerService;
    @RequestMapping("/manager")
    public String signManager(Model model) {
        model.addAttribute("title", "Register - Staff Management System");
        model.addAttribute("manager", new Manager());
        return "manager_registration";//make a page here
    }
    @PostMapping("/do_manager_register")
    public String registerManager(@Valid @ModelAttribute("manager") Manager manager, Model model,BindingResult result, HttpSession session) throws Exception {
        try {
            managerService.registerManager(manager, result, model,session);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "manager_registration";
    }
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, Model model, @RequestParam(value = "agreement",defaultValue = "false")boolean agreement, BindingResult result, HttpSession session) throws Exception {
        try {
            userService.registerUser(user, agreement, result, model,session);
        } catch (Exception e){
            e.printStackTrace();
        }
            return "signup";
    }
}
