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
        return homeRepository.findById(id).orElse(null);
    }

    public Home updateHome(Long id, Home homeDetails) {
        Home home = getHomeById(id);
        home.setName(homeDetails.getName());
        home.setAddress(homeDetails.getAddress());
        home.setTimeZone(homeDetails.getTimeZone());
        return homeRepository.save(home);
    }

    public List<Room> getRoomsByHomeId(Long homeId) {
        Home home = null;
        try {
            home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new Exception("Home not found"));
            return home.getRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteHome(Long id) {
        homeRepository.deleteById(id);
    }
}