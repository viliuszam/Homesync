package me.vilius.homesync.repository;

import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    boolean existsByAddress(String address);
    List<Home> findByUser(User user);
    Optional<Home> findByIdAndUser(Long id, User user);
}
