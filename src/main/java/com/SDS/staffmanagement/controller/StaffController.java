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
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
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
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    public String process_leave(@ModelAttribute LeaveHistory leaveHistory, Model model, HttpSession session,Principal principal) throws ParseException {
        System.out.println(leaveHistory.getFromDate());
        System.out.println(leaveHistory.getToDate());
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        String s = validateDate(leaveHistory.getFromDate(), leaveHistory.getToDate(), userName);
        System.out.println(s);
        List<LeaveHistory> leaveHistoryList= user.getLeave();


          for (LeaveHistory list: leaveHistoryList) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateFrom = dateFormat.parse(list.getFromDate());
            Date dateTo = dateFormat.parse(list.getToDate());
            Date fromDate = dateFormat.parse(leaveHistory.getFromDate());
            List<HolidayCalender> all = holidayCalenderRepository.findAll();

              if ( fromDate.compareTo(dateFrom) >=0 && fromDate.compareTo(dateTo) <=0 )
            {
                    session.setAttribute("message",new Message("Leave already applied .","alert-danger"));
                    return "/Staff/apply_leaves";
            }

             for (HolidayCalender holidayCalender : all) {
                if(leaveHistory.getFromDate().equals(holidayCalender.getDate())){
                    session.setAttribute("message",new Message("It is a public holiday on the existing date .","alert-danger"));
                    return "/Staff/apply_leaves";
                }
              }
          }

        LocalDate fromDate = LocalDate.parse(leaveHistory.getFromDate());
        LocalDate toDate = LocalDate.parse(leaveHistory.getToDate());

        long leaveDuration = ChronoUnit.DAYS.between(fromDate,toDate)+1;



        for(LocalDate date = fromDate; date.isBefore(toDate); date= date.plusDays(1)){
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY )
            {
                leaveDuration--;
            }
        }
        String halfDay = "Half Day";
        if(leaveHistory.getLeaveType().equalsIgnoreCase(halfDay))
        {
            leaveDuration -=0.5;
        }



        System.out.println(leaveDuration);
        leaveHistory.setCurrentMonthLeaves(leaveDuration);
        leaveHistory.setIsApproved(false);
        System.out.println(leaveHistory);

        leaveHistory.setUser(user);
        leaveRepository.save(leaveHistory);
        session.setAttribute("message",new Message("Leave Applied ","alert-success"));
        return "/Staff/apply_leaves";

    }

    private String validateDate(String fromDate, String toDate,String userName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<String> errorMsg = new ArrayList<>();
        LocalDate fromDate1 = LocalDate.parse(fromDate,formatter);
        LocalDate toDate1 = LocalDate.parse(toDate,formatter);

        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);


        if (fromDate1.isBefore(currentMonth) && fromDate1.isAfter(currentMonth.plusMonths(1)))
        {
            return "Leaves can be applied for the current month only.";
        }

        if (fromDate1.isAfter(toDate1))
        {
            return "From date is after To date";
        }

        User userByUserName = userRepository.getUserByUserName(userName);
        System.out.println(userByUserName);
        System.out.println(fromDate);

        if(userByUserName.getLeave().equals(fromDate))
        {
          return "Leave already applied..!!" ;
        }
        return "You are eligible to apply for a leave" ;
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
        String userName = principal.getName();
        User userByUserName = userRepository.getUserByUserName(userName);
    }
}
