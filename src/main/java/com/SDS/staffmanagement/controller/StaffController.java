package com.SDS.staffmanagement.controller;

import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
import com.SDS.staffmanagement.repositories.LeaveRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.HolidayCalenderService;
import com.SDS.staffmanagement.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HolidayCalenderService holidayCalenderService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private LeaveRepository leaveRepository;

    @RequestMapping("/")
    public String dashboard(Model model)
    {
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
    public String process_leave(@ModelAttribute LeaveHistory leaveHistory, Model model, HttpSession session,Principal principal){
//        System.out.println(leaveHistory.getFromDate());
//        System.out.println(leaveHistory.getToDate());
//        LocalDate localDate = LocalDate.now();


        leaveHistory.setIsApproved(false);
        System.out.println(leaveHistory);
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        leaveHistory.setUser(user);
        leaveRepository.save(leaveHistory);
        session.setAttribute("message",new Message("Leave Applied ","alert-success"));
        return "/Staff/apply_leaves";

    }




}
