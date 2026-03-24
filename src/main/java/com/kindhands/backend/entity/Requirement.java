package com.kindhands.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Jar Lombok vaprat asal tar, nasel tar getters/setters manually taka
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String details; // Error line 41 fix karel

    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization; // Error line 23, 38, 40 fix karel

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private User donor; // Error line 34 fix karel
}