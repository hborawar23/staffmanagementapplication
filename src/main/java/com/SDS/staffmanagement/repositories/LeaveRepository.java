package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.entities.LeaveHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<LeaveHistory,Integer> {
}
