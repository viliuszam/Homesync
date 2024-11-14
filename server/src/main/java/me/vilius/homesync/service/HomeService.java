package me.vilius.homesync.service;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.model.User;
import me.vilius.homesync.model.dto.HomeDTO;
import me.vilius.homesync.repository.HomeRepository;
import me.vilius.homesync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Service
public class HomeService {
    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private UserRepository userRepository;

    public Home createHome(Home home) {
        return homeRepository.save(home);
    }

    public List<Home> getAllHomes() {
        return homeRepository.findAll();
    }

    public List<Home> getUserHomes(User user) {
        return homeRepository.findByUser(user);
    }

    public Home getUserHomeById(Long id, User user) {
        return homeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Home not found"));
    }

    public Home getHomeById(Long id) {
        return homeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Home not found with id: " + id));
    }

    public Home createHome(HomeDTO homeDTO, User user) {
        if (homeDTO.getName() == null || homeDTO.getAddress() == null || homeDTO.getTimeZone() == null) {
            throw new IllegalArgumentException("Name, address, and timezone are required");
        }

        if (homeRepository.existsByAddress(homeDTO.getAddress())) {
            throw new IllegalArgumentException("A home with the same address already exists");
        }

        Home home = new Home();
        home.setName(homeDTO.getName());
        home.setAddress(homeDTO.getAddress());
        home.setTimeZone(TimeZone.getTimeZone(homeDTO.getTimeZone()));
        home.setUser(user);
        return homeRepository.save(home);
    }

    public void deleteUserHome(Long id, User user) {
        Home home = homeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Home not found"));
        homeRepository.delete(home);
    }

    public boolean userOwnsHome(Long userId, Long homeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getHomes().stream().anyMatch(home -> home.getId().equals(homeId));
    }

    public Home updateHome(Long id, HomeDTO homeDetails, User user) {
        Home home = getHomeById(id);

        if(!home.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("You don't have access to update this home.");
        }

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