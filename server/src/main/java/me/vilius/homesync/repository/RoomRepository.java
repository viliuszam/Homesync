package me.vilius.homesync.repository;

import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByHomeIn(List<Home> homes);
}