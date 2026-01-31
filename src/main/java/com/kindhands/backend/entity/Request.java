package com.kindhands.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Organization info
    private String organizationName;
    private String organizationType; // orphanage / oldage / ngo
    private String address;
    private String pincode;
    private String contactNumber;
    private String email;

    // Extra criteria
    private Integer minAge;   // for oldage/orphanage (nullable)
    private Integer maxAge;   // nullable

    // Requirement info
    private String itemType;  // food / clothes / books
    private Integer quantity;

    // Status for donor action
    private String status; // PENDING / APPROVED / REJECTED

    public void setStatus(String approved) {
    }

    // getters setters
}
