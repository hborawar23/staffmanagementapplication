package com.staffmanagement.repositories;

import com.staffmanagement.entities.BaseLoginEntity;
import com.staffmanagement.entities.LeaveHistory;
import com.staffmanagement.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager,Integer> {

    Manager findByEmail(String email);

    public List<Manager> findByRoleEquals(String role);

    Manager findById(int id);



    Manager findByMobileNumber(String mobileNumber);


}
