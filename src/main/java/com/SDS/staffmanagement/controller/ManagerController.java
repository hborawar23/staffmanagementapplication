package com.SDS.staffmanagement.controller;
import com.SDS.staffmanagement.entities.LeaveHistory;
import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.helper.Message;
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
import java.util.Arrays;
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
    @GetMapping("/show_project/{id}")
    public String showProject(Model model, @PathVariable("id")int id){
        User user = userRepository.findById(id);
        Project project = user.getProject();
        if(null != project) {
            model.addAttribute("projects", project);
            return "Manager/staff_project";
        } else {
            model.addAttribute("projects",new Project());
            return "Manager/staff_project_Not_found";
        }
    }
    @GetMapping("/assign_project/{id}")
    public String assignProject(Model model,@PathVariable("id")int Id){
        System.out.println(Id);
        User user = userRepository.findById(Id);
        if(null != user ){
            model.addAttribute("user",user);
            List<Project> projects = projectRepository.findAll();
            model.addAttribute("projects",projects);
            return "Manager/add_project" ;
        }
        model.addAttribute("project",new Project());
        return "Manager/add_project" ;
    }
    @PostMapping("/add_project_form/{id}")
    public String addProjectForm(Model model,@PathVariable("id") int Id,
                                 @ModelAttribute("project") Project project, HttpSession session) {
        User user = userRepository.findById(Id);
        if(null != user && null == user.getProject()){
            if(project.getMemberCount()<2){
                user.setProject(project);
                int memberCount = project.getMemberCount();
                project.setMemberCount(++memberCount);
                userRepository.save(user);
                emailService.sendProjectEmail(user.getEmail(),project);
                session.setAttribute("message",new Message("Project added Successfully","alert-success"));
            } else {
                session.setAttribute("message",new Message("Cannot assign more than 2 staff to a  project","alert-danger"));
            }
        } else {
            if(user.getProject().getProjectName().equalsIgnoreCase(project.getProjectName())){
                session.setAttribute("message",new Message("This project is already assigned to this staff.","alert-danger"));
            }else {
                session.setAttribute("message",new Message("Cannot assign more than 1 project","alert-danger"));
            }
        }
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("user",user);
        model.addAttribute("projects",projects);
        model.addAttribute("project",new Project());
        return "Manager/add_project";
    }
    @GetMapping("/approve_leaves")
    public String approve_leave(Model model){
        model.addAttribute("title", "Manager - Approve Leaves");
        List<LeaveHistory> leaveHistoryList = leaveService.getAllLeaves();
        model.addAttribute("allLeaves", leaveHistoryList);
        return "/Manager/approve_leaves";
    }
    @GetMapping("/project_list")
    public String project_list(Model model){
        model.addAttribute("title", "Manager - Project List");
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        return "Manager/project_list";
    }
    @GetMapping("/process_approve/{id}")
    public String process_approve(@PathVariable("id") int id, Model model, Principal principal){
        System.out.println(id);
        String userName = principal.getName();
        Optional<LeaveHistory> byId = leaveRepository.findById(id);
        if (byId.isPresent()){
            User user = byId.get().getUser();
            LeaveHistory leaveHistory = byId.get();
            leaveHistory.setIsApproved(true);
            leaveHistory.setApprovedBy(user.getName());
            leaveRepository.save(leaveHistory);
            emailService.sendProjectApprovedEmail(user,leaveHistory,principal);
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
    @GetMapping("/view_project_details/{id}")
    public String viewProjectDetails(Model model, @PathVariable("id")int id){
        Project project = projectRepository.findByProjectId(id);
        List<User> users = project.getUsers();
        model.addAttribute("project",project);
        model.addAttribute("users",users);
        return "Manager/view_project_details" ;
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
    @GetMapping("/delete_staff_project/{id}")
    public String deleteStaffProject(@PathVariable("id") int Id){
        User user = userRepository.findById(Id);
        int memberCount = user.getProject().getMemberCount();
        int projectId = user.getProject().getProjectId();
        emailService.sendEmailForRemoval(user);
        user.getProject().setMemberCount(--memberCount);
        user.setProject(null);
        userRepository.save(user);
        return "redirect:/manager/view_project_details/"+projectId;
    }
}





