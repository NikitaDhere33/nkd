package com.kindhands.backend.entity;

import com.kindhands.backend.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation_requests")
@Data
public class DonationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemNeeded;
    private String description;
    private String quantity;
    private String organizationName;
    private String organizationAddress;
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private RequestStatus status;

    // --- TABLE साठी आवश्यक फिल्ड्स ---
    private String donorName;      // डोनरचे नाव
    private boolean isPublic = true; // होम पेजवर दाखवण्यासाठी

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime completedAt; // डोनेशन पूर्ण झाल्याची तारीख
}