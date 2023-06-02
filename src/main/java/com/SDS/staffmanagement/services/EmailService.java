package com.SDS.staffmanagement.services;


import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {

    void sendEmail(String email);

    boolean confirmUser(String confirmationToken);

    void sendProjectEmail(String email, Project project);

    void sendProjectApprovedEmail(User user, LeaveHistory leaveHistory);


}
