//package com.SDS.staffmanagement.commonUtils;
//import com.SDS.staffmanagement.entities.LeaveHistory;
//import com.SDS.staffmanagement.repositories.LeaveRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//@Component
//public class ScheduledConfiguration {
//    @Autowired
//    private LeaveHistory leaveHistory;
//
//    @Autowired
//    private LeaveRepository leaveRepository;
//
//    @Scheduled()
//    public void scheduler() throws InterruptedException {
//
//
//
//
//
//
//
//
//
//
//
//
//    }






//
//    }
//    // Create a Timer instance
//    Timer timer = new Timer();
//
//    // Create a TimerTask that will be executed on the 1st day of each month
//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//            // Add 2.5 here (replace with your desired logic)
//            double amountToAdd = 2.5;
//            leaveHistory.setTotalLeaves(amountToAdd);
//            // Add your code here to perform the desired operation
//
//            // For example, update a database, make an API call, etc.
//            System.out.println("Adding " + amountToAdd + " on " + new Date());
//        }
//    };
//
//    // Schedule the task to run on the 1st day of each month at 00:00:00
//    Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.MILLISECOND, 0);
//
//    // Check if the 1st day of the month has already passed
//            if (calendar.getTime().before(new Date())) {
//        calendar.add(Calendar.MONTH, 1); // Set it to the 1st day of the next month
//    }
//
//    // Schedule the task to run monthly
//            timer.schedule(task, calendar.getTime(), 1000L * 60L * 60L * 24L * 30L); // 30 days in milliseconds
//
//
//}



