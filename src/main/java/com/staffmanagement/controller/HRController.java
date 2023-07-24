package com.staffmanagement.controller;
import com.staffmanagement.entities.*;
import com.staffmanagement.entities.*;
import com.staffmanagement.helper.Message;
import com.staffmanagement.helper.UserExcelExporter;
import com.staffmanagement.repositories.*;
import com.staffmanagement.services.*;
import com.staffmanagement.repositories.*;
import com.staffmanagement.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private HolidayCalenderService holidayCalenderService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private HolidayCalenderRepository holidayCalenderRepository;
    @Autowired
    private BaseLoginRepository baseLoginRepository;

    @Autowired
    EmailService emailService;
    @Autowired
    HrService hrService;
    @RequestMapping("/")
    public String view_dashboard(Model model){
        model.addAttribute("title","Admin Dashboard");
        return "HR/hr_dashboard";
    }
    //--------------------------PROJECT----------------------//
    @GetMapping("/modify_project")
    public String modify_project(Model model) {
        model.addAttribute("title", "HR - Modify Project");
        List<Project> allProjects = projectService.getAllProjects();
        if(null != allProjects && allProjects.size()>0){
            for(Project obj : allProjects){
                Manager manager = managerRepository.findById(obj.getManager().getId());
                obj.setManagerName(manager.getName());
                projectRepository.save(obj);
            }
        }
        model.addAttribute("allProjects", allProjects);
        return "HR/modify_project";
    }
    @GetMapping("/add_project")
    public String addProject(Model model) {
        List<Manager> managers = managerRepository.findAll();
        if(null == managers || managers.size()==0){
            Manager manager = new Manager();
            manager.setName("No Manager to select");
            managers.add(manager);
        }
        model.addAttribute("managers", managers);
        model.addAttribute("title", "HR - Add Project");
        model.addAttribute("project", new Project());
        return "/HR/add_project";
    }
    @PostMapping("/process_add_project")
    public String process_add_project(Model model,@ModelAttribute("project") Project project,@ModelAttribute("manager") Manager manager, HttpSession session){
        model.addAttribute("project",new Project());
        String managerName = "";
        if(null != manager){
            project.setManagerName(manager.getName());
        }
         List<String> errorMsgList =  projectService.validateProject(project);
        if(!projectService.isManagerAssignable(project.getManager().getId())){
            errorMsgList.add("Manager already have 2 projects,Assign it to any other manager");
        }
         if(projectService.isProjectNameAlreadyExist(project.getProjectName())){
             errorMsgList.add("Project name already exist");
         }
         if(null == errorMsgList || errorMsgList.size()==0){
             projectRepository.save(project);
             List<Manager> managers = managerRepository.findAll();
             model.addAttribute("managers", managers);
             model.addAttribute("title", "HR - Add Project");
             model.addAttribute("project", new Project());
             session.setAttribute("message",new Message("New Project Added Successfully","alert-success"));
             return "/HR/add_project";
         } else {
             List<Manager> managers = managerRepository.findAll();
//             if(null == managers || managers.size()==0){
//                 Manager manager1 = new Manager();
//                 manager.setName("No Manager to select");
//                 managers.add(manager);
//             }
             model.addAttribute("managers", managers);
             model.addAttribute("title", "HR - Add Project");
             model.addAttribute("project", new Project());
             session.setAttribute("message", new Message(errorMsgList.toString(), "alert-danger"));
             return "/HR/add_project";
         }
    }
    @RequestMapping("/update_project/{id}")
    public String update_project(Model model,@PathVariable("id") Integer id) {
        try {
            model.addAttribute("title", "Update Project");
            Project project = projectRepository.findByProjectId(Integer.valueOf(id));
            if (project != null) {
                model.addAttribute("project", project);
                List<Manager> managers = managerRepository.findAll();
                List<Manager> filteredManager = new ArrayList<>();
                if (managers != null && managers.size() > 0) {
                    for (Manager obj : managers) {
                        if (project.getProjectId() == id) {
                            filteredManager.add(managerRepository.findById(project.getManager().getId()));
                            break;
                        }
                    }
                    for (Manager obj : managers) {
                        if (obj.getId() != project.getManager().getId()) {
                            filteredManager.add(obj);
                        }
                    }
                }
                model.addAttribute("managers", filteredManager);
                return "/HR/update_project";
            }
            return "/HR/update_project";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/HR/update_project";
    }
    @PostMapping("/process_update_project")
    public String process_update_project(@ModelAttribute Project project,HttpSession session,Model model,Principal principal ){
        return hrService.processUpdateProject(project, session, model);
    }
    //-------------------------------------------STAFF-------------------------------------------------//
    @GetMapping("/modify_staff")
    public String modify_staff(Model model){
        model.addAttribute("title", "HR - Modify Staff");
        List<User> allUser = userService.getAllUsers();
        model.addAttribute("allUser", allUser);
        return "HR/modify_staff";
    }

    @GetMapping("/delete_project/{id}")
    public String deleteStaffProject(@PathVariable("id") int Id){
        Project project = projectRepository.findByProjectId(Integer.valueOf(Id));
        project.setManager(null);
////        User user = userRepository.finByProject(project.getProjectId());
//        user.setProject(null);
//        userRepository.save(user);
        projectRepository.delete(project);

        return "";
    }
    @GetMapping("/add_staff")
    public String addStaff(Model model) {
        model.addAttribute("title", "HR - Add Staff");
        model.addAttribute("user", new User());
       return "/HR/add_staff";
    }
    @PostMapping("/add_staff_form")
    public String addStaffForm(@Valid @ModelAttribute("user") User user, Model model, HttpSession session, BindingResult result){
        try {
            userService.registerUser(user, true, result, model,session);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "/HR/add_staff";
    }
    //open update form handler
    @RequestMapping("/update_user/{id}")
    public String update_user(Model model,@PathVariable("id") Integer Id) {
        model.addAttribute("title","Update User");
        Optional<User> byId = userRepository.findById(Id);
        if(byId.isPresent()){
            User user = byId.get();
            model.addAttribute("user",user);
            return "/HR/update_user";
        }
        return "/HR/update_user";
    }
    //update user handler
    @PostMapping("/process_update_user")
    public String updateHandler(@ModelAttribute User user,HttpSession session,Model model,Principal principal){
        if(hrService.updateUser(user,session)){
            session.setAttribute("message",new Message("Updated Successfully","alert-success"));
        }
        model.addAttribute("title","Admin Dashboard");
        return "redirect:/hr/update_user/"+user.getId();
    }
    @GetMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable("id") Integer Id) {
        Optional<User> byId = userRepository.findById(Id);
        if (byId.isPresent()) {
            User user = byId.get();
            userRepository.delete(user);
            return "redirect:/hr/modify_staff/";
        }
        return "redirect:/hr/modify_staff/";
    }
//-------------------------------------------------MANAGER---------------------------------------//
    @GetMapping("/modify_manager")
    public String modify_manager(Model model){
        model.addAttribute("title", "HR - Modify Manager");

        List<Manager> allManager = managerService.getAllManagers();
        model.addAttribute("allManager", allManager);
        return "HR/modify_manager";
    }
    @GetMapping("/add_manager")
    public String addManager(Model model) {
        model.addAttribute("title", "HR - Add Manager");
        model.addAttribute("user", new Manager());
        return "HR/add_manager";
    }
    @PostMapping("/add_manager_form")
    public String addManagerForm(@Valid @ModelAttribute("manager") Manager manager, Model model, HttpSession session, BindingResult result){
        try {
            List<String> errorMsgList = managerService.saveManager(manager);
            if(null == errorMsgList || errorMsgList.size()==0){
                session.setAttribute("message",new Message("Added Successfully","alert-success"));
            } else {
                session.setAttribute("message",new Message(errorMsgList.toString(),"alert-danger"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "HR/add_manager";
    }
    @RequestMapping("/update_manager/{id}")
    public String update_manager(Model model,@PathVariable("id") Integer Id) {
        model.addAttribute("title","Update Manager");
        Optional<Manager> byId = managerRepository.findById(Id);
        if(byId.isPresent()){
            Manager manager = byId.get();
            model.addAttribute("manager",manager);
            return "/HR/update_manager";
        }
        return "/HR/update_manager";
    }
    @PostMapping("/process_update_manager")
    public String processUpdateManager(@ModelAttribute Manager manager,HttpSession session,Model model,Principal principal) {
       List<String> errorMsgList =  managerService.updateManager(manager);
       if(null == errorMsgList || errorMsgList.size()>0){
           session.setAttribute("message",new Message("updated successfully","alert-success"));
       } else {
           session.setAttribute("message",new Message(errorMsgList.toString(),"alert-danger"));
       }
        return "redirect:/hr/update_manager" + manager.getId();
    }
    @GetMapping("/delete_manager/{id}")
    public String deleteManager(@PathVariable("id") Integer Id) {
        Optional<Manager> byId = managerRepository.findById(Id);
        if (byId.isPresent()) {
            Manager manager = byId.get();
            managerRepository.delete(manager);
            return "redirect:/hr/modify_manager";
        }
        return "redirect:/hr/modify_manager";
    }

    @GetMapping("/download_staff_profile")
    public String getAllStaff(Model model) {
        model.addAttribute("title", "HR - Download Staff Profile");
        List<User> allStaff = userService.getAllUsers();
        model.addAttribute("allStaff", allStaff);
        return "HR/download_staff_profile";
    }
    @GetMapping("/download")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users.xlsx";
        response.setHeader(headerKey, headerValue);
        List<User> listUsers = userService.getAllStaff();
        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
        excelExporter.export(response);
    }
    @GetMapping("/calendar")
    public String calendar(Model model){
        model.addAttribute("title", "HR - Calendar");
        List<HolidayCalender> allHolidays = holidayCalenderService.getAllHolidays();
        model.addAttribute("allHolidays",allHolidays);
        return "HR/calendar";
    }
    @GetMapping("/add_holiday")
    public String add_holiday(Model model) {
        model.addAttribute("title", "HR - Add Holiday");
        model.addAttribute("holiday", new HolidayCalender());
        return "/HR/add_holiday";
    }
    @PostMapping("/process_add_holiday")
    public String process_add_staff(Model model,@ModelAttribute("holiday") HolidayCalender holidayCalender,HttpSession session){
        model.addAttribute("holiday",new HolidayCalender());
        List<String> errorMsgList =  validateHoliday(holidayCalender);
        if (null == errorMsgList || !holidayCalenderService.isHolidayAlreadyExist(holidayCalender)){
            holidayCalenderService.addHoliday(holidayCalender);
            session.setAttribute("message",new Message("New Holiday added Successfully","alert-success"));
        } else {
            model.addAttribute("holiday",holidayCalender);
            session.setAttribute("message",new Message(errorMsgList.toString(),"alert-danger"));
        }
        return "/HR/add_holiday";
    }
    private List<String> validateHoliday(HolidayCalender holidayCalender) {
        List<String> errorMsgList = null;
        if(null != holidayCalender){
            if(holidayCalenderService.isHolidayAlreadyExist(holidayCalender)){
                errorMsgList = new ArrayList<>();
                errorMsgList.add("Holiday alreayd exist for the given date: " + holidayCalender.getDate());
            }
        }
        return errorMsgList;
    }
    @GetMapping("/delete_holiday/{id}")
    public String deleteHoliday(@PathVariable("id") Integer Id,Model model) {
        Optional<HolidayCalender> holiday = holidayCalenderRepository.findById(Id);
        if (holiday.isPresent()) {
            holidayCalenderRepository.delete(holiday.get());
        }
        return "redirect:/hr/calendar";
    }
    @RequestMapping("/staff_waiting_list")
    public String staffWaitingList(Model model,HttpSession session){
        List<BaseLoginEntity> waitingList = baseLoginRepository.findByApprovedStatusFalseAndRoleEquals("ROLE_STAFF");
        if (!waitingList.isEmpty()) {
            List<User> staffWaitingList = waitingList.stream().map(BaseLoginEntity::getUser).collect(Collectors.toList());
            model.addAttribute("staffWaitingList", staffWaitingList);
            return "HR/staff_waiting_list";
        }
        session.setAttribute("message",new Message("List is Empty","alert-danger"));
        return "HR/hr_dashboard";
    }
    @RequestMapping("/manager_waiting_list")
    public String managerWaitingList(Model model,HttpSession session) {
        List<BaseLoginEntity> waitingList = baseLoginRepository.findByApprovedStatusFalseAndRoleEquals("ROLE_MANAGER");
        if (!waitingList.isEmpty()) {
            List<Manager> managerWaitingList = waitingList.stream().map(BaseLoginEntity::getManager).collect(Collectors.toList());
            model.addAttribute("managerWaitingList", managerWaitingList);
            return "HR/manager_waiting_list";
        }
        session.setAttribute("message",new Message("List is Empty","alert-danger"));
        return "HR/hr_dashboard";
    }


    @GetMapping("/approve_staff/{id}")
    public String approveStaff(Model model,@PathVariable("id") int id){
        User user = userRepository.findById(id);
        BaseLoginEntity baseLoginEntity = user.getBaseLoginEntity();
        baseLoginEntity.setApprovedStatus(true);
        baseLoginRepository.save(baseLoginEntity);
        emailService.sendEmailForUserApproval(user);
        List<BaseLoginEntity> waitingList = baseLoginRepository.findByApprovedStatusFalseAndRoleEquals("ROLE_STAFF");
        List<User> staffWaitingList = waitingList.stream().map(BaseLoginEntity::getUser).collect(Collectors.toList());
        model.addAttribute("staffWaitingList",staffWaitingList);
        return "HR/staff_waiting_list";
    }

    @GetMapping("/approve_manager/{id}")
    public String approveManager(Model model,@PathVariable("id") int id){
        Manager manager = managerRepository.findById(id);
        BaseLoginEntity baseLoginEntity = manager.getBaseLoginEntity();
        baseLoginEntity.setApprovedStatus(true);
        baseLoginRepository.save(baseLoginEntity);
        emailService.sendEmailForManagerApproval(manager);
        List<BaseLoginEntity> waitingList = baseLoginRepository.findByApprovedStatusFalseAndRoleEquals("ROLE_MANAGER");
        List<Manager> managerWaitingList = waitingList.stream().map(BaseLoginEntity::getManager).collect(Collectors.toList());
        model.addAttribute("managerWaitingList",managerWaitingList);
        return "HR/manager_waiting_list";
    }

}
