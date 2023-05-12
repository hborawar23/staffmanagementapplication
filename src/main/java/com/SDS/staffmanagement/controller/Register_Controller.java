package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.services.EmailService;
import com.SDS.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/register")
public class Register_Controller {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    //handler for registering user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, Model model, @RequestParam(value = "agreement",defaultValue = "false")boolean agreement, BindingResult result, HttpSession session) {
        System.out.println(user + "My User Details..........................................................");
        try {
            if(!agreement)
            {
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }
            if (result.hasErrors()){
                model.addAttribute("user",user);
                return "signup";
            }
            //To check if the user already exist with th given email
            if(userService.existsUserByEmail(user.getEmail()))
            {
                result.addError(new FieldError("user", "email", "an account is already registered with this email address. try with another one!"));
                if (result.hasErrors()) {
                    model.addAttribute("user", user);
                    return "signup";
                }
            }
            user.setPassword(UUID.randomUUID().toString());
            userService.addUser(user);
            emailService.sendEmail(user.getEmail());
            //send email
            model.addAttribute("user",new User());
            session.setAttribute("message",new Message("Registration Successful","alert-success"));
            return "signup";
        }catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message", new Message("Something went wrong !"+e.getMessage(),"alert-error"));
            return "signup";
        }
    }
//
}
