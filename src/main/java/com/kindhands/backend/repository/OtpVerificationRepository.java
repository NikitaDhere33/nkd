package com.kindhands.backend.repository;

import com.kindhands.backend.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByMobileOrderByIdDesc(String mobile);
    void deleteByMobile(String mobile);

    Optional<OtpVerification> findOptionalByMobile(String mobile);
}









