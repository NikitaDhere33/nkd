package com.kindhands.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "organizations"
)
public class Organization {

    // ===== Primary Key =====



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;   // 🔥 THIS IS REQUIRED





        // getters & setters



    // ===== Basic Info =====
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;

    // password API response मध्ये दिसू नये
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String contact;

    private String address;
    private String pincode;

    // NGO / orphanage / oldage
    private String type;

    // document path API मध्ये दिसू नये
    @JsonIgnore
    @Column(name = "document_path")
    private String documentPath;

    // ===== Status =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationStatus status = OrganizationStatus.PENDING;

    // ===== Mapping with User =====
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ===== Forgot Password (optional) =====
    @Column(length = 6)
    private String otp;

    private LocalDateTime otpExpiry;

    // ===== Audit =====
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ===== Getters & Setters =====


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public OrganizationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizationStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
