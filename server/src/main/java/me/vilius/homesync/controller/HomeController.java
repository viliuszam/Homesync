package me.vilius.homesync.controller;

import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homes")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @PostMapping
    public ResponseEntity<?> createHome(@RequestBody Home home) {
        try {
            Home createdHome = homeService.createHome(home);
            return new ResponseEntity<>(createdHome, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<List<Home>> getAllHomes() {
        return new ResponseEntity<>(homeService.getAllHomes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHomeById(@PathVariable Long id) {
            Home home = homeService.getHomeById(id);
            if(home != null){
                return new ResponseEntity<>(home, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
            }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHome(@PathVariable Long id, @RequestBody Home homeDetails) {
        try {
            Home updatedHome = homeService.updateHome(id, homeDetails);
            return new ResponseEntity<>(updatedHome, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHome(@PathVariable Long id) {
        try {
            homeService.deleteHome(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{homeId}/rooms")
    public ResponseEntity<?> getRoomsByHomeId(@PathVariable Long homeId) {
        try {
            List<Room> rooms = homeService.getRoomsByHomeId(homeId);
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
    }
}