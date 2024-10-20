package me.vilius.homesync.service;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.model.dto.HomeDTO;
import me.vilius.homesync.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Service
public class HomeService {
    @Autowired
    private HomeRepository homeRepository;

    public Home createHome(Home home) {
        return homeRepository.save(home);
    }

    public List<Home> getAllHomes() {
        return homeRepository.findAll();
    }

    public Home getHomeById(Long id) {
        return homeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Home not found with id: " + id));
    }

    public Home createHome(HomeDTO homeRequest) {
        if (homeRequest.getName() == null || homeRequest.getAddress() == null || homeRequest.getTimeZone() == null) {
            throw new IllegalArgumentException("Name, address, and timezone are required");
        }

        if (homeRepository.existsByAddress(homeRequest.getAddress())) {
            throw new IllegalArgumentException("A home with the same address already exists");
        }

        Home home = new Home();
        home.setName(homeRequest.getName());
        home.setAddress(homeRequest.getAddress());
        home.setTimeZone(TimeZone.getTimeZone(homeRequest.getTimeZone()));
        home.setCreatedAt(LocalDateTime.now());

        return homeRepository.save(home);
    }

    public Home updateHome(Long id, HomeDTO homeDetails) {
        Home home = getHomeById(id);

        if (homeDetails.getName() == null || homeDetails.getAddress() == null || homeDetails.getTimeZone() == null) {
            throw new IllegalArgumentException("Name, address, and timezone are required");
        }

        if (!home.getAddress().equals(homeDetails.getAddress()) &&
                homeRepository.existsByAddress(homeDetails.getAddress())) {
            throw new IllegalArgumentException("A home with the same address already exists");
        }

        home.setName(homeDetails.getName());
        home.setAddress(homeDetails.getAddress());
        home.setTimeZone(TimeZone.getTimeZone(homeDetails.getTimeZone()));
        return homeRepository.save(home);
    }

    public List<Room> getRoomsByHomeId(Long homeId) {
        Home home = getHomeById(homeId);
        return home.getRooms();
    }

    public void deleteHome(Long id) {
        if (!homeRepository.existsById(id)) {
            throw new EntityNotFoundException("Home not found with id: " + id);
        }
        homeRepository.deleteById(id);
    }
}