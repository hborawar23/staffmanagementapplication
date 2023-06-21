package com.SDS.staffmanagement.controller;

import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.repositories.HolidayCalenderRepository;
import com.SDS.staffmanagement.repositories.LeaveRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.HolidayCalenderService;
import com.SDS.staffmanagement.services.LeaveService;
import com.SDS.staffmanagement.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HolidayCalenderService holidayCalenderService;
    @Autowired
    private HolidayCalenderRepository holidayCalenderRepository;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private LeaveRepository leaveRepository;
    @RequestMapping("/")
    public String dashboard(Model model, Principal principal,HttpSession session) {
        User user = userRepository.getUserByUserName(principal.getName());
        if(null != user){
//            if(StringUtils.isBlank(user.getSkillExperience())){
//                model.addAttribute("user",new User());
//                session.setAttribute("message",new Message("Registration Successful","alert-success"));
//                return "Staff/staff_dashboard";
//            }
        }
        model.addAttribute("title","Staff Dashboard");
        return "Staff/staff_dashboard";
    }
    @RequestMapping("/view_calendar")
    public String openCalendar(Model model){
        model.addAttribute("title","Calendar");
        List<HolidayCalender> allHolidays = holidayCalenderService.getAllHolidays();
        model.addAttribute("allHolidays",allHolidays);
        return "Staff/view_calendar";
    }
    @GetMapping("/apply_leaves_page")
    public String apply_leaves_page(Model model){
        model.addAttribute("title","Apply Leaves");
        return "Staff/apply_leaves";
    }
    @PostMapping("/process_leave")
    public String process_leave(@ModelAttribute LeaveHistory leaveHistory, Model model, HttpSession session,Principal principal) throws ParseException {
        leaveService.processLeave(leaveHistory,session,principal);
        return "/Staff/apply_leaves";
    }
    private String validateDate(String fromDate, String toDate,String userName) {
        return userService.validateDate(fromDate, toDate, userName);
    }
    @GetMapping("/download_profile")
    public String download_profile(Model model){
        model.addAttribute("title","Download Profile");
        return "Staff/download_profile";
    }
    @GetMapping("/export")
    public void exportToPDF(HttpServletResponse response,Principal principal){
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user.pdf";
        response.setHeader(headerKey, headerValue);
        User user = userRepository.getUserByUserName(principal.getName());

    }

    @RequestMapping("/project_details")
    public String projectDetails(Model model,Principal principal){
        model.addAttribute("title","My Project Details");
        User user = userRepository.getUserByUserName(principal.getName());
        if(user.getProject() != null) {
            model.addAttribute("project", user.getProject());
            return "Staff/project_details";
        }else {
            return "Staff/staff_project_Not_found";
        }
    }
}
