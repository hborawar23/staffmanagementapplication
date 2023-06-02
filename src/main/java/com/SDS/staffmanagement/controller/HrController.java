package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.helper.UserExcelExporter;
import com.SDS.staffmanagement.repositories.HolidayCalenderRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.HolidayCalenderService;
import com.SDS.staffmanagement.services.UserService;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/hr")
public class HrController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HolidayCalenderService holidayCalenderService;

    @Autowired
    private HolidayCalenderRepository holidayCalenderRepository;

    @RequestMapping("/")
    public String view_dashboard(Model model)
    {
        model.addAttribute("title","Admin Dashboard");
        return "HR/hr_dashboard";
    }


    @GetMapping("/modify_staff")
    public String modify_staff(Model model){
        model.addAttribute("title", "HR - Modify Staff");
        List<User> allUser = userService.getAllUsers();
        model.addAttribute("allUser", allUser);
        return "HR/modify_staff";
    }

    @GetMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable("id") Integer Id) {
        Optional<User> byId = userRepository.findById(Id);
        if (byId.isPresent()) {
            User user = byId.get();
            userRepository.delete(user);
            return "/HR/modify_staff";
        }
        return "/HR/modify_staff";
    }



    @GetMapping("/add_staff")
    public String addStaff(Model model) {
        model.addAttribute("title", "HR - Add Staff");
        model.addAttribute("user", new User());
       return "/HR/add_staff";
    }

    @PostMapping("/add_staff_form")
    public String addStaffForm(@Valid @ModelAttribute("user") User user, Model model, HttpSession session, BindingResult result){

        if(userService.existsUserByEmail(user.getEmail()))
        {
            result.addError(new FieldError("user", "email", "an account is already registered with this email address. try with another one!"));
            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "/HR/add_staff";
            }
        }

        userService.addUser(user);
        model.addAttribute("user",new User());
        session.setAttribute("message",new Message("New Staff added Successfully","alert-success"));
        return "/HR/add_staff";

    }


    //open update form handler
    @RequestMapping("/update_user/{id}")
    public String update_user(Model model,@PathVariable("id") Integer Id) {
        model.addAttribute("title","Update User");
        Optional<User> byId = userRepository.findById(Id);
        if(byId.isPresent()){
            User user = byId.get();
            model.addAttribute("user",user);
            return "/HR/update_form";
        }
        return "/HR/update_form";
    }

    //update user handler
    @PostMapping("/process_update")
    public String updateHandler(@ModelAttribute User user,HttpSession session,Model model,Principal principal  )
    {
        Optional<User> byId = userRepository.findById(user.getId());
        System.out.println(user.getId() + "-----------------------");
        if(byId.isPresent()){
            User user1 = byId.get();
            user1.setName(user.getName());
            user1.setFatherName(user.getFatherName());
            user1.setMotherName(user.getMotherName());
            user1.setEmail(user.getEmail());
            user1.setGender(user.getGender());
            user1.setPhoto(user.getPhoto());
            user1.setRole(user.getRole());
            user1.setDob(user.getDob());
            user1.setPermanentAddress(user.getPermanentAddress());
            user1.setPresentAddress(user.getPresentAddress());
            user1.setIdentityProof(user.getIdentityProof());
            user1.setMobileNumber(user.getMobileNumber());
            userRepository.save(user1);
            model.addAttribute("title","Admin Dashboard");
            return "/HR/hr_dashboard";
        }
        model.addAttribute("title","Admin Dashboard");
        return "/HR/hr_dashboard";


//        try {
//
//            User user1= this.userRepository.getUserByUserName(principal.getName());
//
//        } catch (Exception e){
//
//        }
//        System.out.println("User Name" + user.getName());
//        return "";
    }

    @GetMapping("/download_staff_profile")
    public String getAllStaff(Model model) {
        model.addAttribute("title", "HR - Download Staff Profile");
        List<User> allStaff = userService.getAllUsers();
        model.addAttribute("allStaff", allStaff);
        return "HR/download_staff_profile";
    }

    @GetMapping("/download")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headerValue = "attachment; filename=users.xlsx";

        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userService.getAllStaff();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
        excelExporter.export(response);
    }

    @GetMapping("/calendar")
    public String calendar(Model model){
        model.addAttribute("title", "HR - Calendar");
        List<HolidayCalender> allHolidays = holidayCalenderService.getAllHolidays();
        model.addAttribute("allHolidays",allHolidays);
        return "HR/calendar";
    }

    @GetMapping("/add_holiday")
    public String add_holiday(Model model) {
        model.addAttribute("title", "HR - Add Holiday");
        model.addAttribute("holiday", new HolidayCalender());
        return "/HR/add_holiday";
    }

    @PostMapping("/process_add_holiday")
    public String process_add_staff(Model model,@ModelAttribute("holiday") HolidayCalender holidayCalender,HttpSession session){
        holidayCalenderService.addHoliday(holidayCalender);
        model.addAttribute("holiday",new HolidayCalender());
        session.setAttribute("message",new Message("New Holiday added Successfully","alert-success"));
        return "/HR/add_holiday";

    }

    @GetMapping("/delete_holiday/{id}")
    public String deleteHoliday(@PathVariable("id") Integer Id) {

        Optional<HolidayCalender> byId = holidayCalenderRepository.findById(Id);

        if (byId.isPresent()) {
            HolidayCalender holidayCalender = byId.get();
            holidayCalenderRepository.delete(holidayCalender);
            return  "/HR/calendar";

        }
        return "/HR/calendar";
    }




}
