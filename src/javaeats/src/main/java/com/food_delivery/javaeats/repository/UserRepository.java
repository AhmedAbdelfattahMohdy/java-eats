package com.food_delivery.javaeats.repository;

import com.food_delivery.javaeats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserRepository, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
