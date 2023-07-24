package com.staffmanagement.services;

import com.staffmanagement.entities.LeaveHistory;
import com.staffmanagement.entities.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

public interface LeaveService {
    void addLeave(LeaveHistory leaveHistory);

    List<LeaveHistory> getAllLeaves();

    public void validateDate(String userName, User user, @ModelAttribute LeaveHistory leaveHistory, Model model) throws ParseException;


    String processLeave(LeaveHistory leaveHistory, HttpSession session, Principal principal) throws ParseException;
}
