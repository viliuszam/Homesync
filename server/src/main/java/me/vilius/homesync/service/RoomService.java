package me.vilius.homesync.service;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Device;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.repository.HomeRepository;
import me.vilius.homesync.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Long homeId, Room room) {
        try {
            Home home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new EntityNotFoundException("Home not found with id: " + homeId));
            room.setHome(home);
            return roomRepository.save(room);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating room: " + e.getMessage(), e);
        }
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room room = getRoomById(id);
        room.setName(roomDetails.getName());
        room.setRoomType(roomDetails.getRoomType());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Device> getDevicesByRoomId(Long roomId) {
        Room room = null;
        try {
            room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new Exception("Room not found"));
            return room.getDevices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
