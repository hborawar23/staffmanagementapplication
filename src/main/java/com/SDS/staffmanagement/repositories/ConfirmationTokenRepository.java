package com.SDS.staffmanagement.repositories;

import com.SDS.staffmanagement.entities.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long>
{
    Optional<ConfirmationTokenEntity> findByConfirmationToken(String confirmationToken);
}


