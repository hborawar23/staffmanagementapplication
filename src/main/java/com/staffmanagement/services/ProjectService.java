package com.staffmanagement.services;

import com.staffmanagement.entities.Project;

import java.util.List;

public interface ProjectService {

//    void addProject(Project project);

    List<Project> getAllProjects();

    boolean isProjectNameAlreadyExist(String projectName);

    boolean isManagerAssignable(int id);

    List<String> validateProject(Project project);
}
