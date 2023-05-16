package com.SDS.staffmanagement.services;
import com.SDS.staffmanagement.commonUtils.ConstantUtils;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {

    }
    @Override
    public Optional<User> findUser(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public boolean existsUserByEmail(String email) {
       return userRepository.existsByEmail(email);
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }


    }
    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<User> getAllStaff() {
        return  userRepository.getAllByRole(ConstantUtils.ROLE_STAFF);
    }


//    @Override
//    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
//        String toAddress = "Himanshi.Borawar@programmers.io";
//        String fromAddress = "staffmanagement.SDS@gmail.com";
//        String senderName = "Staff Management";
//        String subject ="Please verify your registration";
//        String content = "Dear " + user.getName() + " <br>"
//                + "Please click the link below to verify your registration:<br>"
//                +"<h3><a href=\"localhost:\"8080\"register_success\" target=\"_self\">VERIFY</a></h3>"
//                +"Thank you,<br>"
//                +"Staff Management System";
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        helper.setFrom("staffmanagement.SDS@gmail.com","Staff Management");
//        helper.setTo("Himanshi.Borawar@programmers.io");
//        helper.setSubject(subject);
//
//        content = content.replace("[[Name]]",user.getName());
//
//        helper.setText(content, true);
//        mailSender.send(message);    }

//    @Override
//    public void register(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
//            String randomCode = RandomString.make(64);
//            user.setVerificationCode(randomCode);
//            user.setEnabled(false);
//            userRepository.save(user);
//            sendVerificationEmail(user,siteURL);
//    }



}
