package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.email =:email")
    public User getUserByUserName(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    public List<User> findByRoleEquals(String role);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User findById(int id);


    List<User> findByIsApprovedFalse();

    User findByIdentityProof(String aadhar);

    User findByMobileNumber(String mobileNum);

//    User finByProject(int projectId);
}
