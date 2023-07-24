package com.staffmanagement.repositories;

import com.staffmanagement.entities.BaseLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseLoginRepository extends JpaRepository<BaseLoginEntity,Integer> {
//    BaseLoginEntity findByEmail(String email);
    BaseLoginEntity findById(int id);

    List<BaseLoginEntity> findByApprovedStatusFalseAndRoleEquals(String role);

    List<BaseLoginEntity> findByRoleAndApprovedStatus(String role,boolean approvedStatus);
}
