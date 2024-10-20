package me.vilius.homesync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import me.vilius.homesync.model.Device;
import me.vilius.homesync.model.dto.DeviceDTO;
import me.vilius.homesync.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Device", description = "Device management APIs")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Operation(summary = "Create a new device", description = "Creates a new device in a specific room")
    @ApiResponse(responseCode = "201", description = "Device created successfully",
            content = @Content(schema = @Schema(implementation = DeviceDTO.class)))
    @ApiResponse(responseCode = "400", description = "Malformed request")
    @ApiResponse(responseCode = "422", description = "Invalid payload data")
    @PostMapping
    public ResponseEntity<?> createDevice(@Parameter(description = "ID of the room to add the device to") @RequestParam Long roomId,
                                          @Parameter(description = "Device details") @Valid @RequestBody DeviceDTO deviceDTO) {
        try {
            Device createdDevice = deviceService.createDevice(roomId, deviceDTO.toEntity());
            return new ResponseEntity<>(DeviceDTO.fromEntity(createdDevice), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Get all devices", description = "Retrieves a list of all devices")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of devices",
            content = @Content(schema = @Schema(implementation = Device.class)))
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return new ResponseEntity<>(deviceService.getAllDevices(), HttpStatus.OK);
    }

    @Operation(summary = "Get a device by ID", description = "Retrieves a device by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of device",
            content = @Content(schema = @Schema(implementation = Device.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDeviceById(@Parameter(description = "ID of the device to retrieve") @PathVariable Long id) {
        Device device;
        try{
            device = deviceService.getDeviceById(id);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        }
            return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @Operation(summary = "Update a device", description = "Updates an existing device")
    @ApiResponse(responseCode = "200", description = "Device updated successfully",
            content = @Content(schema = @Schema(implementation = DeviceDTO.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "422", description = "Invalid payload")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDevice(@Parameter(description = "ID of the device to update") @PathVariable Long id,
                                          @Parameter(description = "Updated device details") @Valid @RequestBody DeviceDTO deviceDTO) {
        try {
            Device updatedDevice = deviceService.updateDevice(id, deviceDTO.toEntity());
            return new ResponseEntity<>(DeviceDTO.fromEntity(updatedDevice), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Delete a device", description = "Deletes an existing device")
    @ApiResponse(responseCode = "204", description = "Device deleted successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevice(@Parameter(description = "ID of the device to delete")
                                              @PathVariable Long id) {
        try {
            deviceService.deleteDevice(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Device not found", HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
