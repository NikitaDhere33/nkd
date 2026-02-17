package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Donate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donate, Long> {

    // Public donation history (only allowed ones)
    List<Donate> findByPublicHistoryTrue();

    // Donor personal history
    List<Donate> findByDonorId(Long donorId);

    // Organization donations
    List<Donate> findByOrganizationId(Long organizationId);
}
