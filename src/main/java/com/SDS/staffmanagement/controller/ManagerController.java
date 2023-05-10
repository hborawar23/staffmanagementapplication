package com.SDS.staffmanagement.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @RequestMapping("/home")
    public String dashboard()
    {
        return "manager_dashboard";
    }
}
