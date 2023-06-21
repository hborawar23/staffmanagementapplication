package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;

import java.util.List;

public interface ProjectService {

//    void addProject(Project project);

    List<Project> getAllProjects();

    boolean isProjectNameAlreadyExist(String projectName);

    boolean isManagerAssignable(int id);

    List<String> validateProject(Project project);
}
