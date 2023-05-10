package com.SDS.staffmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hr")
public class HrController {
    @RequestMapping("/home")
    public String dashboard()
    {
        return "hr_dashboard";
    }
}
