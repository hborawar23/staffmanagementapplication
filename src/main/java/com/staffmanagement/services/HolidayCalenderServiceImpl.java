package com.staffmanagement.services;

import com.staffmanagement.entities.HolidayCalender;
import com.staffmanagement.repositories.HolidayCalenderRepository;
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

    @Override
    public boolean isHolidayAlreadyExist(HolidayCalender holidayCalender) {
       HolidayCalender holidayCalender1 =  holidayCalenderRepository.findByDate(holidayCalender.getDate());
       if(holidayCalender1 != null){
           return true;
       } else {
           return false;
       }
    }
}
