package com.staffmanagement.repositories;

import com.staffmanagement.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

    Project findByProjectName(String projectName);

    Project findByProjectId(Integer valueOf);
}
