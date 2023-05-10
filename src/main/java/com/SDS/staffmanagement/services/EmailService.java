package com.SDS.staffmanagement.services;


public interface EmailService {

    void sendEmail(String email);

    boolean confirmUser(String confirmationToken);
}
