package com.staffmanagement.services;

import com.staffmanagement.entities.HolidayCalender;

import java.util.List;

public interface HolidayCalenderService {

    void addHoliday(HolidayCalender holidayCalender);

    List<HolidayCalender> getAllHolidays();

    boolean isHolidayAlreadyExist(HolidayCalender holidayCalender);


}
