package com.SDS.staffmanagement.services;


import com.SDS.staffmanagement.entities.BaseLoginEntity;
import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.Principal;

public interface EmailService {

    void sendEmail(String email,String password,String name);

    boolean confirmUser(String confirmationToken);

    void sendProjectEmail(String email, Project project);

    void sendEmailForApproval(BaseLoginEntity baseLoginEntity);

    void sendEmailForRemoval(User user);
//
    void sendProjectApprovedEmail(User user, LeaveHistory leaveHistory, Principal principal);


}
