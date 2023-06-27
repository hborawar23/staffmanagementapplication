package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.entities.LeaveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<LeaveHistory,Integer> {

//    boolean existsByDate(String date);

    @Query("select u from LeaveHistory u where u.fromDate =:fromDate")
    public List<LeaveHistory> getFromDate(String fromDate);

    @Query("select u from LeaveHistory u where u.toDate =:toDate")
    public List<LeaveHistory> getToDate(String toDate);

    public List<LeaveHistory> findLeaveByUser(int id);


}
