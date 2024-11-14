package me.vilius.homesync.repository;

import me.vilius.homesync.model.Device;
import me.vilius.homesync.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByRoom_HomeIn(List<Home> homes);
}