package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;


public interface UserService {
    void addUser(User user);

    void updateUser(User user);

    Optional<User> findUser(String email);

    boolean existsUserByEmail(String email);

//    void sendVerificationEmail(User user,String siteURL) throws MessagingException, UnsupportedEncodingException;

//    void register (User user,String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);





}
