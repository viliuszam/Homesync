package me.vilius.homesync.repository;

import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByIdAndHomesContaining(Long userId, Home home);
}