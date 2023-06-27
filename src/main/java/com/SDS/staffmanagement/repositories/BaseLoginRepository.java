package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.BaseLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseLoginRepository extends JpaRepository<BaseLoginEntity,Integer> {
    BaseLoginEntity findByEmail(String email);
    BaseLoginEntity findById(int id);

    List<BaseLoginEntity> findByApprovedStatusFalseAndRoleEquals(String role);
}
