package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.Manager;
import com.SDS.staffmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,Integer> {

    Manager findByEmail(String email);

    public List<Manager> findByRoleEquals(String role);

    Manager findById(int id);

    List<Manager> findByIsApprovedFalse();

    Manager findByMobileNumber(String mobileNumber);
}
