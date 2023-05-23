package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.Project;
import com.SDS.staffmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

}
