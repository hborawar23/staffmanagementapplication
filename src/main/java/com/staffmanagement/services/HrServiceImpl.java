package com.staffmanagement.services;

import com.staffmanagement.entities.Manager;
import com.staffmanagement.entities.Project;
import com.staffmanagement.entities.User;
import com.staffmanagement.helper.Message;
import com.staffmanagement.repositories.ManagerRepository;
import com.staffmanagement.repositories.ProjectRepository;
import com.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class HrServiceImpl implements HrService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Override
    public boolean updateUser(User user, HttpSession session) {
        List<String> errorMsgList = userService.isValidateUser(user);
        User userToUpdate = userRepository.findById(user.getId());
        isSomethingAlreadyExist(user,userToUpdate,errorMsgList);
        if (null == errorMsgList || errorMsgList.size()==0) {
            userToUpdate.setName(user.getName());
            userToUpdate.setGender(user.getGender());
            userToUpdate.setMobileNumber(user.getMobileNumber());
            userToUpdate.setIdentityProof(user.getIdentityProof());
            userToUpdate.setPresentAddress(user.getPresentAddress());
            userToUpdate.setDob(user.getDob());
            userToUpdate.setRole(user.getRole());
            userToUpdate.setPermanentAddress(user.getPermanentAddress());

            userToUpdate.setEmail(user.getEmail());
            userRepository.save(userToUpdate);
            return true;
        } else {
            session.setAttribute("message", new Message(errorMsgList.toString(), "alert-danger"));
            return false;
        }
    }

    private List<String> isSomethingAlreadyExist(User user,User userToUpdate,List<String> errorMsgList) {
        if (!userToUpdate.getEmail().equalsIgnoreCase(user.getEmail()) && userService.existsUserByEmail(user.getEmail())) {
            errorMsgList.add("Email already exist");
        }
        if(!userToUpdate.getMobileNumber().equalsIgnoreCase(user.getMobileNumber())
                && userService.existsUserByMobileNumber(user.getMobileNumber())){
            errorMsgList.add("Mobile number already exist");
        }
        if(!userToUpdate.getIdentityProof().equalsIgnoreCase(user.getIdentityProof())
                && userService.existsUserByAadharNumber(user.getIdentityProof())){
            errorMsgList.add("Aadhar number already exist");
        }
        return errorMsgList;
    }

    @Override
    public boolean updateProject(Project project, HttpSession session,List<String> errorMsgList) {
        Project projectToUpdate = projectRepository.findByProjectId(project.getProjectId());
        projectToUpdate.setProjectDescription(project.getProjectDescription());
        projectToUpdate.setManagerName(project.getManagerName());
        projectToUpdate.setManager(project.getManager());
        projectRepository.save(projectToUpdate);
        return true;
    }

    @Override
    public String processUpdateProject(Project project, HttpSession session, Model model) {
        Manager byId = managerRepository.findById(project.getManager().getId());
        project.setManagerName(byId.getName());
        List<String> errorMsgList =  projectService.validateProject(project);
        try {
            Project projectToUpdate = projectRepository.findByProjectId(project.getProjectId());
            if(projectToUpdate.getManager().getId() != project.getManager().getId() && !projectService.isManagerAssignable(project.getManager().getId())){
                errorMsgList.add("Manager already have 2 projects,Assign it to any other manager");
            }
            if(!project.getProjectName().equalsIgnoreCase(projectToUpdate.getProjectName()) && projectService.isProjectNameAlreadyExist(project.getProjectName())){
                errorMsgList.add("project name already exist");
            }
            if((null== errorMsgList || errorMsgList.size()==0) && updateProject(project, session,errorMsgList)){
                session.setAttribute("message",new Message("Updated Successfully","alert-success"));
            } else {
                session.setAttribute("message",new Message(errorMsgList.toString(),"alert-danger"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("title","Admin Dashboard");
        return "redirect:/hr/update_project/" + project.getProjectId();
    }
}
