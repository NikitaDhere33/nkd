package com.kindhands.backend.repository;

import com.kindhands.backend.entity.DonationRequest;
import com.kindhands.backend.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
    List<DonationRequest> findByStatus(RequestStatus status);
}
