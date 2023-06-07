package com.SDS.staffmanagement.services;


import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService{

    @Autowired
    private LeaveRepository leaveRepository;


    @Override
    public void addLeave(LeaveHistory leaveHistory) {
        leaveRepository.save(leaveHistory);
    }

    @Override
    public List<LeaveHistory> getAllLeaves() {
        List<LeaveHistory> leaveHistoryList = leaveRepository.findAll();
        List<LeaveHistory> leaveHistoryList1 = new ArrayList<>();
        for (LeaveHistory leavehistory: leaveHistoryList) {
            if (leavehistory.getIsApproved().equals(false)){
                leaveHistoryList1.add(leavehistory);
            }
        }
        return leaveHistoryList1;
    }




}
