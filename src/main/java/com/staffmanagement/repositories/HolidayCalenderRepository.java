package com.staffmanagement.repositories;

import com.staffmanagement.entities.HolidayCalender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayCalenderRepository extends JpaRepository<HolidayCalender,Integer> {
    HolidayCalender findByDate(String date);
}
