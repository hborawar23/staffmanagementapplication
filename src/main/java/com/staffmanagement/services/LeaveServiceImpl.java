package com.staffmanagement.services;
import com.staffmanagement.commonUtils.ConstantUtils;
import com.staffmanagement.entities.HolidayCalender;
import com.staffmanagement.entities.LeaveHistory;
import com.staffmanagement.entities.User;
import com.staffmanagement.helper.Message;
import com.staffmanagement.repositories.HolidayCalenderRepository;
import com.staffmanagement.repositories.LeaveRepository;
import com.staffmanagement.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HolidayCalenderRepository holidayCalenderRepository;


    @Override
    public void addLeave(LeaveHistory leaveHistory) {
        leaveRepository.save(leaveHistory);
    }

    @Override
    public List<LeaveHistory> getAllLeaves() {
        List<LeaveHistory> leaveHistoryList = leaveRepository.findAll();
        List<LeaveHistory> leaveHistoryList1 = new ArrayList<>();
        for (LeaveHistory leavehistory : leaveHistoryList) {
            if (leavehistory.getIsApproved().equals(false)) {
                leaveHistoryList1.add(leavehistory);
            }
        }
        return leaveHistoryList1;
    }

    @Override
    public void validateDate(String userName, User user, @ModelAttribute LeaveHistory leaveHistory, Model model) throws ParseException {
        List<LeaveHistory> leaveHistoryList = user.getLeave();

        for (LeaveHistory list : leaveHistoryList) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateFrom = dateFormat.parse(list.getFromDate());
            Date dateTo = dateFormat.parse(list.getToDate());
            Date fromDate = dateFormat.parse(leaveHistory.getFromDate());
            List<HolidayCalender> all = holidayCalenderRepository.findAll();
        }

    }

    @Override
    public String processLeave(LeaveHistory leaveHistory, HttpSession session, Principal principal) throws ParseException {

        String errorMsg = isValidateLeave(leaveHistory);
        if(StringUtils.isNotBlank(errorMsg)){
            session.setAttribute("message", new Message(errorMsg, "alert-danger"));
            return "/Staff/apply_leaves";
        }
        User user = userRepository.getUserByUserName(principal.getName());
        // checking if leave already applied, if yes --> leave Already applied
        if(isLeaveApplicable(leaveHistory,principal)){
            // eligible only to apply current month leave
                if(isOnlyCurrentMonthLeave(leaveHistory)) {
                    // checking if company holiday already exitis, if yes--> company holiday already exist
                    if(!isCompanyHolidayExist(leaveHistory,principal)){
                        //check if only weekend days applied
                      if(!isOnlyWeekendExist(leaveHistory)){
                          //check if leave is for one day, HALF/FULL
                          if((!leaveHistory.getFromDate().equals(leaveHistory.getToDate()))){
                              if(!ConstantUtils.HALF_DAY.equalsIgnoreCase(leaveHistory.getLeaveType())){
                                  saveLeavesExcludingWeekend(leaveHistory,user);
                                  session.setAttribute("message", new Message("Leave Applied","type-success"));
                                  return "/Staff/apply_leaves";
                              } else {
                                  session.setAttribute("message", new Message("Half day is not applicable", "alert-danger"));
                                  return "/Staff/apply_leaves";
                              }
                          } else {
                              saveLeavesExcludingWeekend(leaveHistory,user);
                              session.setAttribute("message", new Message("Leave Applied", "type-success"));
                              return "/Staff/apply_leaves";
                          }
                      } else {
                          session.setAttribute("message", new Message("Leave not applicable for only weekends", "alert-danger"));
                          return "/Staff/apply_leaves";
                      }

                     } else {
                        session.setAttribute("message", new Message("It is a public holiday on the existing date .", "alert-danger"));
                        return "/Staff/apply_leaves";
                     }
                } else {
                    session.setAttribute("message", new Message("Leave can only be applied for the current month.", "alert-danger"));
                    return "/Staff/apply_leaves";
                }

        } else {
            session.setAttribute("message", new Message("Leave already applied .", "alert-danger"));
            return "/Staff/apply_leaves";
        }
    }
    private String isValidateLeave(LeaveHistory leaveHistory) {
        String errorMsg = null;
        if(null != leaveHistory){
            if(StringUtils.isBlank(leaveHistory.getFromDate())){
                errorMsg = "Start date cannot be null or blank";
                return errorMsg;
            }
            if(StringUtils.isBlank(leaveHistory.getToDate())){
                errorMsg = "End date cannot be null or blank";
                return errorMsg;
            }
            if(StringUtils.isBlank(leaveHistory.getDescription())){
                errorMsg = "Description cannot be  blank";
                return errorMsg;
            }

        }
       return errorMsg;
    }
    private boolean isLeaveApplicable(LeaveHistory leaveHistory,Principal principal) {
        boolean isLeaveApplicable = true;
        try {
            List<LeaveHistory> userLeaveHistory = userRepository.getUserByUserName(principal.getName()).getLeave();
            LocalDate fromDate = LocalDate.parse(leaveHistory.getFromDate());
            LocalDate toDate = LocalDate.parse(leaveHistory.getToDate());
            if(null != userLeaveHistory && userLeaveHistory.size()>0 ){
                for (LeaveHistory alreadyAppliedLeave :userLeaveHistory) {
                    LocalDate existingFromDate = LocalDate.parse(alreadyAppliedLeave.getFromDate());
                    LocalDate existingToDate = LocalDate.parse(alreadyAppliedLeave.getToDate());
                    if(fromDate.isEqual(existingFromDate)){
                        isLeaveApplicable = false;
                    }
                    if(fromDate.compareTo(existingFromDate) >= 0 && fromDate.compareTo(existingToDate) <= 0){
                        isLeaveApplicable = false;
                    }
                    if(toDate.isEqual(existingToDate)){
                        isLeaveApplicable = false;
                    }
                    if(toDate.isBefore(existingToDate) && toDate.isAfter(existingFromDate)){
                        isLeaveApplicable = false;
                    }
                }
            }
        } catch (Exception e ){
            e.printStackTrace();
        }
        return  isLeaveApplicable;
    }

    private boolean isHalfDayApplicable(LeaveHistory leaveHistory) {
        boolean isHalfDayAppliable = true;
        if(leaveHistory.getFromDate().equals(leaveHistory.getToDate())){
            if (ConstantUtils.HALF_DAY.equalsIgnoreCase(leaveHistory.getLeaveType())) {
                isHalfDayAppliable = true;
            }
        }
        return  isHalfDayAppliable;
    }
    private void saveLeavesExcludingWeekend(LeaveHistory leaveHistory,User user) {
        LocalDate fromDate = LocalDate.parse(leaveHistory.getFromDate());
        LocalDate toDate = LocalDate.parse(leaveHistory.getToDate());
        int totalDuration = 0;
        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)){
            if(currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY){
                totalDuration++;
            }
            currentDate = currentDate.plusDays(1);
        }
        leaveHistory.setCurrentMonthLeaves(totalDuration);
        leaveHistory.setIsApproved(false);
        leaveHistory.setUser(user);
        leaveRepository.save(leaveHistory);
    }
    private boolean isLeaveAlreadyExist(LeaveHistory leaveHistory, Principal principal) {
        boolean isLeaveHolidayExist = false;
        try {
           String userName = principal.getName();
           List<LeaveHistory> userLeaveHistory = userRepository.getUserByUserName(userName).getLeave();
           if (null != userLeaveHistory && userLeaveHistory.size() > 0) {
               for (LeaveHistory leave : userLeaveHistory) {
                   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                   Date dateFrom = dateFormat.parse(leave.getFromDate());
                   Date dateTo = dateFormat.parse(leave.getToDate());
                   Date fromDate = dateFormat.parse(leaveHistory.getFromDate());
                   Date toDate = dateFormat.parse(leaveHistory.getToDate());
                   List<HolidayCalender> companyHolidayList = holidayCalenderRepository.findAll();
                   if ((fromDate.compareTo(dateFrom) >= 0 && fromDate.compareTo(dateTo) <= 0) ||
                           (toDate.compareTo(dateFrom) >= 0 && toDate.compareTo(dateTo) <= 0)) {
                       isLeaveHolidayExist = true;
                       break;
                   }
               }
           }
       } catch (Exception e){
           e.printStackTrace();
       }
       return isLeaveHolidayExist;
    }

    private boolean isOnlyCurrentMonthLeave(LeaveHistory leaveHistory) {
        boolean isOnlyCurrentMonthLeave = false;
        try {
               LocalDate fromDate = LocalDate.parse( leaveHistory.getFromDate());
               LocalDate toDate = LocalDate.parse(leaveHistory.getToDate());
               LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
               LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

              if(fromDate.compareTo(firstDayOfMonth) >= 0 && toDate.compareTo(lastDayOfMonth) <= 0 ){
                  isOnlyCurrentMonthLeave = true;
              }
        }catch (Exception e){
            e.printStackTrace();
        }
        return isOnlyCurrentMonthLeave;
    }
    private boolean isOnlyWeekendExist(LeaveHistory leaveHistory) {
        LocalDate fromDate = LocalDate.parse(leaveHistory.getFromDate());
        LocalDate toDate = LocalDate.parse(leaveHistory.getToDate());
        boolean isOnlyWeekendExist = false;
        if(fromDate.getDayOfWeek() == DayOfWeek.SATURDAY && toDate.getDayOfWeek() == DayOfWeek.SUNDAY){
          isOnlyWeekendExist = true;
        }
        return isOnlyWeekendExist;
    }
    private Boolean isCompanyHolidayExist(LeaveHistory leaveHistory,Principal principal) {
        boolean isCompanyHolidayExist = false;
        try{
            String userName = principal.getName();
            List<LeaveHistory> userLeaveHistory = userRepository.getUserByUserName(userName).getLeave();
            if (null != userLeaveHistory && userLeaveHistory.size() > 0) {
                for (LeaveHistory leave : userLeaveHistory) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateFrom = dateFormat.parse(leave.getFromDate());
                    Date dateTo = dateFormat.parse(leave.getToDate());
                    Date fromDate = dateFormat.parse(leaveHistory.getFromDate());
                    List<HolidayCalender> companyHolidayList = holidayCalenderRepository.findAll();
                    for (HolidayCalender holidayCalender : companyHolidayList) {
                        if (leaveHistory.getFromDate().equals(holidayCalender.getDate())) {
                            isCompanyHolidayExist=true;
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  isCompanyHolidayExist;
    }
}
