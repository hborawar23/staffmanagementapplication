package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.ConfirmationTokenEntity;
import com.SDS.staffmanagement.entities.User;

import com.SDS.staffmanagement.repositories.ConfirmationTokenRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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
    public EmailServiceImple(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
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
}
