package com.staffmanagement.services;

import com.staffmanagement.entities.Manager;
import com.staffmanagement.entities.Project;
import com.staffmanagement.repositories.ManagerRepository;
import com.staffmanagement.repositories.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public boolean isProjectNameAlreadyExist(String projectName) {
        if(null != projectRepository.findByProjectName(projectName)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isManagerAssignable(int id) {
        Manager manager = managerRepository.findById(id);
        if(null != manager && manager.getProjects().size()<2){
            return true;
        } else {
            return false;
        }
    }
    public List<String> validateProject(Project project) {
        List<String> errorMsgList = new ArrayList<>();
        if(null != project){
            if(StringUtils.isBlank(project.getProjectName())){
                errorMsgList.add("project name cannot be null or blank");
            }
            if(StringUtils.isBlank(project.getProjectDescription())){
                errorMsgList.add("project description cannot be null or blank");
            }
            if(StringUtils.isBlank(project.getManagerName()) || "No Manager to select".equalsIgnoreCase(project.getManagerName())){
                errorMsgList.add("Please select a manager to add project");
            }
        }
        return errorMsgList;
    }
}
