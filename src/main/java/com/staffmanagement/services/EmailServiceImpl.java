package com.staffmanagement.services;

import com.staffmanagement.entities.*;

import com.staffmanagement.entities.*;
import com.staffmanagement.repositories.ConfirmationTokenRepository;
import com.staffmanagement.repositories.ProjectRepository;
import com.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;
    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String mail,String password,String name) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setCc("Himanshi.Borawar@programmers.io");
        simpleMailMessage.setSubject("Registration Successfull");
        simpleMailMessage.setText("Hi " +name + " Your registration is successfully completed, Please login once approved by HR");
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public boolean confirmUser(String confirmationToken) {
        Optional<ConfirmationTokenEntity> byConfirmationToken = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (byConfirmationToken.isPresent()) {
            Optional<User> user = userService.findUser(byConfirmationToken.get().getUser().getEmail());
            user.get().setEnabled(true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public void sendProjectEmail(String email, Project project) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setCc("Himanshi.Borawar@programmers.io");
        simpleMailMessage.setSubject("New Project Assigned");
        simpleMailMessage.setText("Hi " + userRepository.getUserByUserName(email).getName() + " You have been assigned a new project."
                + "\n Project name  - " + project.getProjectName() +
                "\nProject description - " + project.getProjectDescription()
                +"\nYour Project Manager is- " + project.getManager().getName());
        javaMailSender.send(simpleMailMessage);

    }

    @Override
    public void sendEmailForUserApproval(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setCc("Himanshi.Borawar@programmers.io");
        simpleMailMessage.setSubject("Request approved ");
        simpleMailMessage.setText("Hi " + user.getName() + ",\n\nYour registration request has been approved by HR, Please login with given password:  " +"1234");
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendEmailForManagerApproval(Manager manager) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(manager.getEmail());
        simpleMailMessage.setCc("Himanshi.Borawar@programmers.io");
        simpleMailMessage.setSubject("Request approved ");
        simpleMailMessage.setText("Hi " + manager.getName() + ",\n\nYour registration request has been approved by HR, Please login with given password:  " +"1234");
        javaMailSender.send(simpleMailMessage);

    }

    @Override
    public void sendEmailForRemoval(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setCc("Himanshi.Borawar@programmers.io");
        simpleMailMessage.setSubject("Removed From Project ");
        simpleMailMessage.setText("Hi " +user.getName() + " Your have been removed from the assigned project: " +user.getProject().getProjectName()
                                 +"\n Please contact your project manager( "+user.getProject().getManager().getName()+") for more info");
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendProjectApprovedEmail(User user, LeaveHistory leaveHistory, Principal principal) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Leave Approved");
            mailMessage.setCc("Himanshi.Borawar@programmers.io");
            mailMessage.setText("Hello, "+user.getName()+" \n"
                    + "\n Your Leave from : "+ leaveHistory.getFromDate()
                    + "\n To : "+leaveHistory.getToDate()
                    + "\n is Approved"
                    + "\n by Manager : " +principal.getName());
            javaMailSender.send(mailMessage);

    }
}
