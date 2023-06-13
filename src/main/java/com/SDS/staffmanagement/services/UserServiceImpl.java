package com.SDS.staffmanagement.services;
import com.SDS.staffmanagement.commonUtils.ConstantUtils;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }
    @Override
    public String registerUser(User user, Boolean agreement, MultipartFile file, BindingResult result, Model model, HttpSession session) throws Exception {
        List<String> errorMsgList =  isValidateUser(user);
        if(null != errorMsgList && errorMsgList.size()==0){

            if(!agreement)
            {
                throw new Exception("You have not agreed the terms and conditions");
            }
            if (result.hasErrors()){
                model.addAttribute("user",user);
                return "signup";
            }
            if(!file.isEmpty()){
                System.out.println("Image is empty");
                //if the file is empty then try our message
               // user.setPhoto(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded");
            }
            if(userService.existsUserByEmail(user.getEmail())){
                result.addError(new FieldError("user", "email", "an account is already registered with this email address. try with another one!"));
                if (result.hasErrors()) {
                    model.addAttribute("user", user);
                    return "signup";
                }
            }

            user.setPassword(bCryptPasswordEncoder.encode("1234"));
            userService.addUser(user);
            emailService.sendEmail(user.getEmail(),"1234");

            //send email
            model.addAttribute("user",new User());
            session.setAttribute("message",new Message("Registration Successful","alert-success"));
            return "signup";
        } else {

            model.addAttribute("user",new User());
            session.setAttribute("message",new Message(errorMsgList.toString(),"alert-danger"));
            return "signup";
        }

    }


    private List<String> isValidateUser(User user) {
        List<String> errorMsgList = new ArrayList<>();
        if(user != null){
            if(StringUtils.isBlank(user.getEmail())){
               errorMsgList.add("email cannot be blank");
            }
            if(StringUtils.isBlank(user.getMobileNumber()) || !(user.getMobileNumber().length() == 10)){
                errorMsgList.add("Mobile number cannot be null or blank and should be be of 10 digit");
            }
            if(StringUtils.isBlank(user.getIdentityProof()) || !(user.getIdentityProof().length() == 12)){
                errorMsgList.add("Aadhar number cannot be null or blank should be of 12 digit");
            }
        }

        return errorMsgList;
    }

    @Override
    public void updateUser(User user) {

    }
    @Override
    public Optional<User> findUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
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
        return  userRepository.findByRoleEquals(ConstantUtils.ROLE_STAFF);
    }





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