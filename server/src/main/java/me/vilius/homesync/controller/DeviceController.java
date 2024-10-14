package me.vilius.homesync.controller;

import me.vilius.homesync.model.Device;
import me.vilius.homesync.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<?> createDevice(@RequestParam Long roomId, @RequestBody Device device) {
        try {
            Device createdDevice = deviceService.createDevice(roomId, device);
            return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return new ResponseEntity<>(deviceService.getAllDevices(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeviceById(@PathVariable Long id) {
        Device device = deviceService.getDeviceById(id);
        if(device != null){
            return new ResponseEntity<>(device, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable Long id, @RequestBody Device deviceDetails) {
        try {
            Device updatedDevice = deviceService.updateDevice(id, deviceDetails);
            return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
        } catch (InvalidConfigurationPropertyValueException e) {
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long id) {
        try {
            deviceService.deleteDevice(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        }
    }
}
