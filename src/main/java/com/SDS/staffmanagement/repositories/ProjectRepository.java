package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

    Project findByProjectName(String projectName);

    Project findByProjectId(Integer valueOf);
}
