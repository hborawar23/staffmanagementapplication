package com.SDS.staffmanagement.services;
import com.SDS.staffmanagement.commonUtils.ConstantUtils;
import com.SDS.staffmanagement.entities.BaseLoginEntity;
import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.repositories.BaseLoginRepository;
import com.SDS.staffmanagement.repositories.ManagerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService{
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private BaseLoginRepository baseLoginRepository;
    @Override
    public String registerManager(Manager manager, BindingResult result, Model model, HttpSession session) {
        if (managerService.existByEmail(manager.getEmail())) {
            result.addError(new FieldError("manager", "email", "an account is already registered with this email address. try with another one!"));
            if (result.hasErrors()) {
                model.addAttribute("manager", manager);
                return "manager_registration";
            }
            return "manager_registration";
        } else {
            manager.setPassword(bCryptPasswordEncoder.encode("1234"));
            manager.setRole("ROLE_MANAGER");
            managerRepository.save(manager);
            emailService.sendEmail(manager.getEmail(), "1234",manager.getName());
            savingIntoBaseLoginEntity(manager);
            model.addAttribute("manager",new Manager());
            session.setAttribute("message",new Message("Registration  Successfull","alert-success"));
            return "manager_registration";
        }
    }


    @Override
    public boolean existByEmail(String email) {
        if(null != managerRepository.findByEmail(email)){
            return true;
        } else {
            return false;
        }
    }
    @Override
    public List<Manager> getAllManagers() {
        return  managerRepository.findAll();
    }
    @Override
    public List<Manager> getAllInWaitingList() {
        return managerRepository.findByIsApprovedFalse();
    }
    @Override
    public List<String> saveManager(Manager manager) {
        List<String> errorMsgList = isValidateManager(manager);
        errorMsgList = isEmailPhoneAlreadyExist(manager);
        if(null == errorMsgList || errorMsgList.size()==0){
            managerRepository.save(manager);
        }
        return errorMsgList;
    }
    @Override
    public List<String> updateManager(Manager manager) {
        List<String> errorMsgList = isValidateManager(manager);
        Manager managerToUpdate = managerRepository.findById(manager.getId());
         if(!managerToUpdate.getEmail().equalsIgnoreCase(manager.getEmail())
                 && null!=managerRepository.findByEmail(manager.getEmail())){
             errorMsgList.add("Email already exist");
         }
         if(!managerToUpdate.getMobileNumber().equalsIgnoreCase(manager.getMobileNumber()) &&
         null != managerRepository.findByMobileNumber(manager.getMobileNumber())){
             errorMsgList.add("Mobile number already exist");
         }
        return errorMsgList;
    }

    @Override
    public void savingIntoBaseLoginEntity(Manager manager) {
        BaseLoginEntity obj = new BaseLoginEntity();
        obj.setName(manager.getName());
        obj.setEmail(manager.getEmail());
        obj.setRole(manager.getRole());
        obj.setPassword(manager.getPassword());
        obj.setApprovedStatus(false);
        baseLoginRepository.save(obj);
    }

    private List<String> isEmailPhoneAlreadyExist(Manager manager) {
        List<String> errorMsgList = new ArrayList<>();
        if(null!=managerRepository.findByEmail(manager.getEmail())){
            errorMsgList.add("Email already exist");
        }
        if(null != managerRepository.findByMobileNumber(manager.getMobileNumber())){
            errorMsgList.add("Mobile number already exist");
        }
        return errorMsgList;
    }
    private List<String> isValidateManager(Manager manager) {
        List<String> errorMsgList = new ArrayList<>();
        if(manager != null){
            if(StringUtils.isBlank(manager.getMobileNumber())){
                errorMsgList.add("Mobile number cannot be blank");
            }
            if(StringUtils.isBlank(manager.getName())){
                errorMsgList.add("Full name cannot be blank");
            }
            if(StringUtils.isBlank(manager.getEmail())){
                errorMsgList.add("email cannot be blank");
            }
        }
        return errorMsgList;
    }
}
