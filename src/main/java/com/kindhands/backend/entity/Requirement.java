package com.kindhands.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requirements")
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    private String title;
    private Integer quantity;
    private String description;

    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    private LocalDateTime createdAt;

    // getters & setters
    public Long getId() { return id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public RequirementStatus getStatus() { return status; }
    public void setStatus(RequirementStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
