package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.HolidayCalender;
import com.SDS.staffmanagement.repositories.HolidayCalenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayCalenderServiceImpl implements HolidayCalenderService{

    @Autowired
    private HolidayCalenderRepository holidayCalenderRepository;

    @Override
    public void addHoliday(HolidayCalender holidayCalender) {
        holidayCalenderRepository.save(holidayCalender);
    }

    @Override
    public List<HolidayCalender> getAllHolidays() {
        return holidayCalenderRepository.findAll();
    }
}
