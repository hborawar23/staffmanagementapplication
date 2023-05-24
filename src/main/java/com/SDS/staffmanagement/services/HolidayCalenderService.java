package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.HolidayCalender;

import java.util.List;

public interface HolidayCalenderService {

    void addHoliday(HolidayCalender holidayCalender);

    List<HolidayCalender> getAllHolidays();
}
