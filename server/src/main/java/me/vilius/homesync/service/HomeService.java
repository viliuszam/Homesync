package me.vilius.homesync.service;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Home updateHome(Long id, Home homeDetails) {
        Home home = getHomeById(id);
        home.setName(homeDetails.getName());
        home.setAddress(homeDetails.getAddress());
        home.setTimeZone(homeDetails.getTimeZone());
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