package com.kindhands.backend.repository;


import com.kindhands.backend.entity.Donate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donate, Long> {
}
