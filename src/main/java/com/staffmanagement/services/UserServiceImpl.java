package com.staffmanagement.services;
import com.staffmanagement.commonUtils.ConstantUtils;
import com.staffmanagement.entities.BaseLoginEntity;
import com.staffmanagement.entities.Manager;
import com.staffmanagement.entities.User;
import com.staffmanagement.helper.Message;
import com.staffmanagement.repositories.BaseLoginRepository;
import com.staffmanagement.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private BaseLoginRepository baseLoginRepository;
    @Override
    public void addUser(User user) {

        userRepository.save(user);
    }
    @Override
    public String registerUser(User user, Boolean agreement, BindingResult result, Model model, HttpSession session) throws Exception {
        List<String> errorMsgList =  isValidateUser(user);
        if(userService.existsUserByEmail(user.getEmail())){
            errorMsgList.add("Email already exist");
        }
        if(userService.existsUserByAadharNumber(user.getIdentityProof())){
            errorMsgList.add("Aadhar number already exist");
        }
        if(userService.existsUserByMobileNumber(user.getMobileNumber())){
            errorMsgList.add("Mobile number already exist");
        }
        if(!userService.isDateOfBirthValid(user.getDob())){
            errorMsgList.add("Date of Birth Should not be greater than Current Date.");

        }

        if(null != errorMsgList && errorMsgList.isEmpty()){
            if(Boolean.FALSE.equals(agreement)) {
                throw new Exception("You have not agreed the terms and conditions");
            }
            if (result.hasErrors()) {
                model.addAttribute("user",user);
                return "signup";
            }
            user.setPassword(bCryptPasswordEncoder.encode("1234"));
//            BaseLoginEntity baseLoginEntity = user.getBaseLoginEntity();
//            baseLoginRepository.save(baseLoginEntity);
            savingIntoBaseLoginEntity(user,"ROLE_STAFF");
//            user.setBaseLoginEntity(baseLoginRepository.findById(user.getId()));
            userService.addUser(user);
            emailService.sendEmail(user.getEmail(),"1234",user.getName());
            //send email
            model.addAttribute("user",new User());
            session.setAttribute("message",new Message("Registration Successful","alert-success"));
            return "signup";
        } else {
            addErrorsToResult(result,errorMsgList);
            session.setAttribute("message",new Message(errorMsgList.toString(),"alert-danger"));
            model.addAttribute("user",user);
            return "signup";
        }
    }

    @Override
    public void savingIntoBaseLoginEntity(User user,String role) {
        BaseLoginEntity obj = new BaseLoginEntity();
        obj.setRole(role);
        obj.setApprovedStatus(false);

        baseLoginRepository.save(obj);
        user.setBaseLoginEntity(obj);
        userRepository.save(user);
    }

    @Override
    public boolean isDateOfBirthValid(String dob) {
        LocalDate dateOfBirth = LocalDate.parse(dob);
        LocalDate currentDate = LocalDate.now();
        return dateOfBirth.isBefore(currentDate);
    }

    private void addErrorsToResult(BindingResult result, List<String> errorMsgList) {
        for(String erroMsg : errorMsgList){
            if(erroMsg.contains(ConstantUtils.PERMANENT_ADDRESS)){
                result.addError(new FieldError("user", "z", ""));
            }
        }
    }
    public List<String> isValidateUser(User user) {
        List<String> errorMsgList = new ArrayList<>();
        if(user != null){
            if(StringUtils.isBlank(user.getEmail())){
               errorMsgList.add("email cannot be blank");
            }
            if(StringUtils.isBlank(user.getMobileNumber())){
                errorMsgList.add("Mobile number cannot be blank  should be be of 10 digit");
            }
            if(!(user.getMobileNumber().length() == 10)){
                errorMsgList.add("Mobile number should be be of 10 digit");
            }
            if(StringUtils.isBlank(user.getIdentityProof())){
                errorMsgList.add("Aadhar number cannot be blank ");
            }
            if(!(user.getIdentityProof().length() == 12)){
                errorMsgList.add("Aadhar number should be of 12 digit");
            }
            if(StringUtils.isBlank(user.getPresentAddress()) ){
                errorMsgList.add("Present address cannot be blank");
            }
            if(StringUtils.isBlank(user.getPermanentAddress())){
                errorMsgList.add("Permanent address cannot be blank");
            }
            if(StringUtils.isBlank(user.getDob())){
                errorMsgList.add("DOB can not null or blank");
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
        return Optional.ofNullable(userRepository.findById(id));
    }
    @Override
    public boolean existsUserByEmail(String email) {
       return userRepository.existsByEmail(email);
    }
    @Override
    public boolean existsUserByAadharNumber(String aadhar) {
        if(null != userRepository.findByIdentityProof(aadhar)){
            return true;
        }
        return false;
    }
    @Override
    public boolean existsUserByMobileNumber(String mobileNum) {
        if(null != userRepository.findByMobileNumber(mobileNum)){
            return true;
        }
        return false;
    }
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
//            user.setVerificationCode(null);
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
    @Override
    public List<User> getAllInWaitingStaff() {
        List<BaseLoginEntity> role_staff = baseLoginRepository.findByRoleAndApprovedStatus("ROLE_STAFF", false);
        List<User> staffList=new ArrayList<>();
        for(BaseLoginEntity baseLoginEntity:role_staff){
            staffList.add(baseLoginEntity.getUser());
        }
        return staffList;
    }
    @Override
    public String validateDate(String fromDate, String toDate, String userName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> errorMsg = new ArrayList<>();
        LocalDate fromDate1 = LocalDate.parse(fromDate,formatter);
        LocalDate toDate1 = LocalDate.parse(toDate,formatter);
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        if (fromDate1.isBefore(currentMonth) && fromDate1.isAfter(currentMonth.plusMonths(1))) {
            return "Leaves can be applied for the current month only.";
        }
        if (fromDate1.isAfter(toDate1)) {
            return "From date is after To date";
        }
        User userByUserName = userRepository.getUserByUserName(userName);
        System.out.println(userByUserName);
        System.out.println(fromDate);
        if(userByUserName.getLeave().equals(fromDate)) {
            return "Leave already applied..!!";
        }
        return "You are eligible to apply for a leave";
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