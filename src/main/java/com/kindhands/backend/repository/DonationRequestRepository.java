package com.kindhands.backend.repository;

import com.kindhands.backend.entity.DonationRequest;
import com.kindhands.backend.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {

    // डोनर डॅशबोर्डसाठी पेंडिंग लिस्ट
    List<DonationRequest> findByStatus(RequestStatus status);

    // 🔥 होम पेजवरील 'Impact Table' साठी: फक्त COMPLETED आणि Public डोनेशन्स, तारखेनुसार (Latest First)
    List<DonationRequest> findByStatusAndIsPublicTrueOrderByCreatedAtDesc(RequestStatus status);
}