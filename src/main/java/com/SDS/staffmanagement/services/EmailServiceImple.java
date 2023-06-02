package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.ConfirmationTokenEntity;
import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;

import com.SDS.staffmanagement.repositories.ConfirmationTokenRepository;
import com.SDS.staffmanagement.repositories.ProjectRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EmailServiceImple implements EmailService {

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
    public EmailServiceImple(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setCc("Himanshi.Borawar@programmers.io");
        simpleMailMessage.setSubject("Please verify the email");
        simpleMailMessage.setText("To confirm your account, please click here : " + "http://localhost:8080/confirm-account?token=" + "<br>" + "Your pass word is :" + userRepository.getUserByUserName(mail).getPassword());
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
                + "\n Project name  - " + project.getName() +
                "\nProject description - " + project.getProjectDescription()  );
        javaMailSender.send(simpleMailMessage);

    }

    @Override
    public void sendProjectApprovedEmail(User user, LeaveHistory leaveHistory) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Leave Approved");
            mailMessage.setCc("Himanshi.Borawar@programmers.io");
            mailMessage.setText("Hello, \n"
                    + "\n Your Leave from : "+ leaveHistory.getFromDate()
                    + "\n To : "+leaveHistory.getToDate()
                    + "\n is Approved"
                    + "\n by Manager : " + user.getName());
            javaMailSender.send(mailMessage);

    }
}
