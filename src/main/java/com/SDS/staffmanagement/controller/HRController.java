package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.helper.UserExcelExporter;
import com.SDS.staffmanagement.repositories.HolidayCalenderRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.HolidayCalenderService;
import com.SDS.staffmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hr")
public class HRController {

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
        try {
            User oldUser = this.userRepository.findById(user.getId()).get();

//            if (!file.isEmpty()){
//                //delete old
//                File deleteFile = new ClassPathResource("static/image").getFile();
//               // File file1 = new File(deleteFile,oldUser.getPhoto());
//                //file1.delete();
//
//                //update
//                File saveFile = new ClassPathResource("static/image").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
//                //user.setPhoto(file.getOriginalFilename());
//                } else {
//                      //  user.setPhoto(oldUser.getPhoto());
//            }
//                User userByUserName = userRepository.getUserByUserName(principal.getName());
//                userByUserName.setName(String.valueOf(userByUserName));
//                    this.userRepository.findById(user.getId()).get();
                userRepository.save(user);
                session.setAttribute("message",new Message("Updated Successfully","alert-success"));
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("title","Admin Dashboard");
        return "redirect:/hr/modify_staff";
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
