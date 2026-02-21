package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    // 🔹 Admin use – pending / approved / rejected orgs
    List<Organization> findByStatus(OrganizationStatus status);

    // 🔹 Login / Forgot password (organization email check)
    Optional<Organization> findByEmail(String email);


    // 🔹 Registration duplicate check
    Optional<Organization> findByContact(String contact);

    // ✅ EXTRA (SAFE & USEFUL)
    //boolean existsByEmail(String email);
    boolean existsByContact(String contact);

}
