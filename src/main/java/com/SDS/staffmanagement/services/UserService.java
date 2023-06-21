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
    String registerUser(User user, Boolean agreement,  BindingResult result, Model model, HttpSession session) throws Exception;
    List<String> isValidateUser(User user);
    void updateUser(User user);
    Optional<User> findUser(String email);
    Optional<User> findUserById(int id);
    boolean existsUserByEmail(String email);
    boolean existsUserByAadharNumber(String aadhar);
    boolean existsUserByMobileNumber(String mobileNum);
    boolean verify(String verificationCode);
    List<User> getAllUsers();
    List<User> getAllStaff();
    List<User> getAllInWaitingStaff();
    String validateDate(String fromDate, String toDate, String userName);

    void savingIntoBaseLoginEntity(User user,String role);


}
//    void sendVerificationEmail(User user,String siteURL) throws MessagingException, UnsupportedEncodingException;

//    void register (User user,String siteURL) throws MessagingException, UnsupportedEncodingException;