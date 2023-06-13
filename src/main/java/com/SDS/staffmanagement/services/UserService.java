package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.entities.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;


public interface UserService {
    void addUser(User user);

    String registerUser(User user, Boolean agreement, MultipartFile file, BindingResult result, Model model, HttpSession session) throws Exception;
    void updateUser(User user);

    Optional<User> findUser(String email);

    Optional<User> findUserById(int id);

    boolean existsUserByEmail(String email);
    boolean verify(String verificationCode);

    List<User> getAllUsers();



    List<User> getAllStaff();

}













//    void sendVerificationEmail(User user,String siteURL) throws MessagingException, UnsupportedEncodingException;

//    void register (User user,String siteURL) throws MessagingException, UnsupportedEncodingException;