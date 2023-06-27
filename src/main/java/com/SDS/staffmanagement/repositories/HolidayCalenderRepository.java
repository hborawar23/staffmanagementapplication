package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.HolidayCalender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayCalenderRepository extends JpaRepository<HolidayCalender,Integer> {
    HolidayCalender findByDate(String date);
}
