package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findByStatus(OrganizationStatus status);

    Optional<Organization> findByEmail(String email);

    Optional<Organization> findByUserId(Long userId);
    Optional<Organization> findByContact(String contact);

}
