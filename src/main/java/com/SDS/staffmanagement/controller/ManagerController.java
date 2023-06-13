package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.UserExcelExporter;
import com.SDS.staffmanagement.repositories.LeaveRepository;
import com.SDS.staffmanagement.repositories.ProjectRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import com.SDS.staffmanagement.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String dashboard() {
        return "Manager/manager_dashboard";
    }

    // add manager
    @RequestMapping("/signup/manager")
    public String signManager(Model model) {
        model.addAttribute("title", "Register - Staff Management System");
        model.addAttribute("manager", new Manager());
        return "signup";//make a page here
    }
    @PostMapping("/manager/do_register")
    public String registerManager(@Valid @ModelAttribute("manager") Manager manager, Model model, @RequestParam(value = "agreement",defaultValue = "false")boolean agreement, BindingResult result, HttpSession session) throws Exception {
        try {
            managerService.registerManager(manager, agreement, result, model,session);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "signup";
    }

    // update manager
    // delete manager
    @GetMapping("/download_staff_profile")
    public String getAllStaff(Model model) {
        model.addAttribute("title", "Manager - Download Staff Profile");
        List<User> allStaff = userService.getAllStaff();
        model.addAttribute("allStaff", allStaff);
        return "Manager/download_staff_profile";
    }

    @GetMapping("/download/{id}")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headerValue = "attachment; filename=users.xlsx";

        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userService.getAllStaff();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
        excelExporter.export(response);
    }

    @GetMapping("/modify_project")
    public String getAllStaffProject(Model model) {
        model.addAttribute("title", "Manager - Modify Project");
        List<User> allStaff = userService.getAllStaff();
        model.addAttribute("allStaff", allStaff);
        return "Manager/modify_project";
    }

//    @GetMapping("/show_project/{id}")
//    public String showProject(Model model, @PathVariable("id")int id){
//        Optional<User> byId = userRepository.findById(id);
//        if (byId.isPresent())
//        {
//            User user = byId.get();
//            List<Project> projects = user.getProjects();
//            System.out.println(projects);
//            model.addAttribute("projects",projects);
//        }
//        return "Manager/staff_project" ;
//
//    }
//
//    @GetMapping("/assign_project/{id}")
//    public String assignProject(Model model,@PathVariable("id")int Id){
//        System.out.println(Id);
//        Optional<User> byId = userRepository.findById(Id);
//        if(byId.isPresent()){
//            User user = byId.get();
//            model.addAttribute("user",user);
//            model.addAttribute("project",new Project());
//            return "Manager/add_project" ;
//        }
//        model.addAttribute("project",new Project());
//        return "Manager/add_project" ;
//    }


//    @PostMapping("/add_project_form/{id}")
//    public String addProjectForm(Model model,  @PathVariable("id") int Id, @ModelAttribute("project") Project project, HttpSession session)
//    {
//        Optional<User> byId = userRepository.findById(Id);
//        if(byId.isPresent()){
//            User user = byId.get();
//            project.setUser(user);
//            user.getProjects().add(project);
//            userRepository.save(user);
//            emailService.sendProjectEmail(user.getEmail(),project);
//            model.addAttribute("user",user);
//            model.addAttribute("project",new Project());
//            return "Manager/add_project";
//
//        }
//        model.addAttribute("project",new Project());
//        return "Manager/add_project";
//    }

    @GetMapping("/approve_leaves")
    public String approve_leave(Model model){
        model.addAttribute("title", "Manager - Approve Leaves");
        List<LeaveHistory> leaveHistoryList = leaveService.getAllLeaves();
        model.addAttribute("allLeaves", leaveHistoryList);
        return "/Manager/approve_leaves";
    }

    @GetMapping("/process_approve/{id}")
    public String process_approve(@PathVariable("id") int id, Model model, Principal principal){
        System.out.println(id);
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
//        Optional<LeaveHistory> byId = leaveRepository.findById(id);

        Optional<LeaveHistory> byId = leaveRepository.findById(id);
        if (byId.isPresent()){
            LeaveHistory leaveHistory = byId.get();
            leaveHistory.setIsApproved(true);
            leaveHistory.setApprovedBy(user.getName());
            leaveRepository.save(leaveHistory);
//            emailService.sendProjectApprovedEmail(user,leaveHistory);
            model.addAttribute("title", "Manager - Approve Leaves");
            List<LeaveHistory> leaveHistoryList = leaveService.getAllLeaves();

            model.addAttribute("allLeaves", leaveHistoryList);
            return "/Manager/approve_leaves";

        }
        model.addAttribute("title", "Manager - Approve Leaves");
        List<LeaveHistory> leaveHistoryList = leaveService.getAllLeaves();
        model.addAttribute("allLeaves", leaveHistoryList);
        return "/Manager/approve_leaves";
    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteProject(@PathVariable("id") Integer Id,Model model) {
//        Optional<Project> byId = projectRepository.findById(Id);
//        if (byId.isPresent()) {
//            Project project = byId.get();
//            projectRepository.delete(project);
//            return "redirect:/manager/modify_project";
//        }
//        return "redirect:/manager/modify_project";
//    }

}





