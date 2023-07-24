package com.staffmanagement.services;


import com.staffmanagement.entities.*;

import java.security.Principal;

public interface EmailService {

    void sendEmail(String email,String password,String name);

    boolean confirmUser(String confirmationToken);

    void sendProjectEmail(String email, Project project);

    void sendEmailForUserApproval(User user);

    void sendEmailForManagerApproval(Manager manager);

    void sendEmailForRemoval(User user);
//
    void sendProjectApprovedEmail(User user, LeaveHistory leaveHistory, Principal principal);


}
