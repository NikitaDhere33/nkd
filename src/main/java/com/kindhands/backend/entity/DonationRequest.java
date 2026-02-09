package com.kindhands.backend.entity;
import com.kindhands.backend.enums.RequestStatus;
import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "donation_requests")
public class DonationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Organization who created request
    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private String organizationName;

    @Column(nullable = false)
    private String organizationAddress;

    @Column(nullable = false)
    private String itemNeeded; // Food, Clothes, Books, Money

    private String description;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.OPEN;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime completedAt;

    // Getters & Setters
    public Long getId() { return id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getOrganizationAddress() { return organizationAddress; }
    public void setOrganizationAddress(String organizationAddress) { this.organizationAddress = organizationAddress; }

    public String getItemNeeded() { return itemNeeded; }
    public void setItemNeeded(String itemNeeded) { this.itemNeeded = itemNeeded; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
