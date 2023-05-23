package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.UserService;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.Multipart;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    @GetMapping("/modify_staff")
    public String modify_staff(Model model){
        model.addAttribute("title", "HR - Modify Staff");
        List<User> allUser = userService.getAllUsers();
        model.addAttribute("allUser", allUser);
        return "HR/modify_staff";
    }

    @GetMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable("id") Integer Id) {
        Optional<User> byId = userRepository.findById(Id);
        if (byId.isPresent()) {
            User user = byId.get();
            userRepository.delete(user);
            return "/HR/modify_staff";
        }
        return "/HR/modify_staff";
    }



    @GetMapping("/add_staff")
    public String addStaff(Model model) {
        model.addAttribute("title", "HR - Add Staff");
        model.addAttribute("user", new User());
       return "/HR/add_staff";
    }

    @PostMapping("/add_staff_form")
    public String addStaffForm(@Valid @ModelAttribute("user") User user, Model model, HttpSession session, BindingResult result){

        if(userService.existsUserByEmail(user.getEmail()))
        {
            result.addError(new FieldError("user", "email", "an account is already registered with this email address. try with another one!"));
            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "/HR/add_staff";
            }
        }

        userService.addUser(user);
        model.addAttribute("user",new User());
        session.setAttribute("message",new Message("New Staff added Successfully","alert-success"));
        return "/HR/add_staff";

    }


    //open update form handler
    @RequestMapping("/update_user/{id}")
    public String update_user(Model model,@PathVariable("id") Integer Id) {
        model.addAttribute("title","Update User");
        Optional<User> byId = userRepository.findById(Id);
        if(byId.isPresent()){
            User user = byId.get();
            model.addAttribute("user",user);
            return "/HR/update_form";
        }
        return "/HR/update_form";
    }

    //update user handler
    @PostMapping("/process_update")
    public String updateHandler(@ModelAttribute User user,HttpSession session,Model model,Principal principal  )
    {
        try {

            User user1= this.userRepository.getUserByUserName(principal.getName());

        } catch (Exception e){

        }
        System.out.println("User Name" + user.getName());
        return "";
    }



}
