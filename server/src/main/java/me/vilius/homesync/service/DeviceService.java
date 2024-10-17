package me.vilius.homesync.service;

import jakarta.persistence.EntityNotFoundException;
import me.vilius.homesync.model.Device;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.repository.DeviceRepository;
import me.vilius.homesync.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public Device createDevice(Long roomId, Device device) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));
        device.setRoom(room);
        return deviceRepository.save(device);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + id));
    }

    public Device updateDevice(Long id, Device deviceDetails) {
        Device device = getDeviceById(id);
        device.setName(deviceDetails.getName());
        device.setDeviceType(deviceDetails.getDeviceType());
        device.setManufacturer(deviceDetails.getManufacturer());
        device.setState(deviceDetails.isState());
        device.setPowerConsumption(deviceDetails.getPowerConsumption());
        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);
    }
}