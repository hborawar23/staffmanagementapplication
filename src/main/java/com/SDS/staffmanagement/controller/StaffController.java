package com.SDS.staffmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @RequestMapping("/home")
    public String dashboard()
    {
        return "staff_dashboard";
    }
}
