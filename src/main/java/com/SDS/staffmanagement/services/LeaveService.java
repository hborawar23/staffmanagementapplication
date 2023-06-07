package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.User;

import java.util.List;

public interface LeaveService {
    void addLeave(LeaveHistory leaveHistory);

    List<LeaveHistory> getAllLeaves();


}
