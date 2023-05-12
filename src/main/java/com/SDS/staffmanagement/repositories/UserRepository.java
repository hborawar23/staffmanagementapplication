package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.email =:email")
    public User getUserByUserName(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


}
