package com.kindhands.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class Donate {

    // ================= PRIMARY KEY =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= DONOR INFO =================
    @Column(nullable = false)
    private Long donorId;          // User table ID

    @Column(nullable = false)
    private String donorName;

    @Column(nullable = false)
    private String donorEmail;

    // Donor permission for public donation history
    @Column(nullable = false)
    private boolean publicHistory = false;

    // ================= ORGANIZATION INFO =================
    @Column(nullable = false)
    private Long organizationId;

    private String organizationName;
    private String organizationType;
    private String organizationAddress;

    // ================= DONATION DETAILS =================
    // Clothes, Food, Books, Medical, Toys etc.
    @Column(nullable = false)
    private String donationType;

    private String itemName;
    private String category;     // Educational, Fiction, etc.
    private String foodType;
    private LocalDate expiryDate;
    private String kitType;
    private Integer quantity;

    // ================= REQUEST & STATUS =================
    // When organization posted request
    private LocalDateTime requestPostedAt;

    // When donor donated
    private LocalDateTime donatedAt;

    // When donation was completed
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;

    // ================= AUDIT =================
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public boolean isPublicHistory() {
        return publicHistory;
    }

    public void setPublicHistory(boolean publicHistory) {
        this.publicHistory = publicHistory;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getKitType() {
        return kitType;
    }

    public void setKitType(String kitType) {
        this.kitType = kitType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getRequestPostedAt() {
        return requestPostedAt;
    }

    public void setRequestPostedAt(LocalDateTime requestPostedAt) {
        this.requestPostedAt = requestPostedAt;
    }

    public LocalDateTime getDonatedAt() {
        return donatedAt;
    }

    public void setDonatedAt(LocalDateTime donatedAt) {
        this.donatedAt = donatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
