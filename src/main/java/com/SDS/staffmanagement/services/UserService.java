package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    void addUser(User user);

    void updateUser(User user);

    Optional<User> findUser(String email);

    boolean existsUserByEmail(String email);

//    void sendVerificationEmail(User user,String siteURL) throws MessagingException, UnsupportedEncodingException;

//    void register (User user,String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);

    List<User> getAllUsers();

    List<User> getAllStaff();





}
