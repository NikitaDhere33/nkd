package com.kindhands.backend.repository;

import com.kindhands.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 Login / Forgot password (main use)
    Optional<User> findByEmail(String email);

    // 📱 Optional – mobile login / future use
    Optional<User> findByMobile(String mobile);

    // ✅ Register validation
    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}
