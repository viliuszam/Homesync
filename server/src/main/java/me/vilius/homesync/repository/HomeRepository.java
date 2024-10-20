package me.vilius.homesync.repository;

import me.vilius.homesync.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    boolean existsByAddress(String address);
}
