package me.vilius.homesync.service;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Device;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.model.dto.RoomDTO;
import me.vilius.homesync.repository.HomeRepository;
import me.vilius.homesync.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Long homeId, RoomDTO roomDTO) {
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new EntityNotFoundException("Home not found with id: " + homeId));

        Room room = new Room();
        room.setName(roomDTO.getName());
        room.setRoomType(roomDTO.getRoomType());
        room.setHome(home);

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }

    public Room updateRoom(Long id, RoomDTO roomDTO) {
        Room room = getRoomById(id);
        room.setName(roomDTO.getName());
        room.setRoomType(roomDTO.getRoomType());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    public List<Device> getDevicesByRoomId(Long roomId) {
        Room room = getRoomById(roomId);
        return room.getDevices();
    }
}
