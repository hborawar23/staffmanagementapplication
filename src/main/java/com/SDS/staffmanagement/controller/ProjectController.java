package com.SDS.staffmanagement.controller;

import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.repositories.ManagerRepository;
import com.SDS.staffmanagement.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ManagerService managerService;
    @RequestMapping("/project")
    public String signManager(Model model) {
        model.addAttribute("title", "Register - Staff Management System");
        Manager manager = new Manager();
        manager.setId(2);
        manager.setMobileNumber("1247895126");
        manager.setGender("Male");
        manager.setSkills("JAVA");
        manager.setName("Nirmal Khatri");
        managerRepository.save(manager);
        List<Manager> managers = managerRepository.findAll();
        model.addAttribute("managers", managers);
        return "Project/add_project";//make a page here
    }
}
