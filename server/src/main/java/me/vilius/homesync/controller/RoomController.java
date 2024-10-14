package me.vilius.homesync.controller;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Device;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestParam Long homeId, @RequestBody Room room) {
        try {
            Room createdRoom = roomService.createRoom(homeId, room);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating room: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);

        if(room != null){
            return new ResponseEntity<>(room, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        try {
            Room updatedRoom = roomService.updateRoom(id, roomDetails);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }// catch (Exception e) {
          //  return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
        //}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{roomId}/devices")
    public ResponseEntity<?> getDevicesByRoomId(@PathVariable Long roomId) {
        try {
            List<Device> devices = roomService.getDevicesByRoomId(roomId);
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
    }
}