package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectSecviceImple implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void addProject(Project project) {
        projectRepository.save(project);


    }
}
