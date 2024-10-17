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
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Room", description = "Room management APIs")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Operation(summary = "Create a new room", description = "Creates a new room in a specific home")
    @ApiResponse(responseCode = "201", description = "Room created successfully",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "400", description = "Malformed request")
    @ApiResponse(responseCode = "422", description = "Invalid payload data")
    @PostMapping
    public ResponseEntity<?> createRoom(
            @Parameter(description = "ID of the home to add the room to") @RequestParam Long homeId,
            @Parameter(description = "Room details") @Valid @RequestBody Room room) {
        try {
            Room createdRoom = roomService.createRoom(homeId, room);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Get all rooms", description = "Retrieves a list of all rooms")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of rooms",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @Operation(summary = "Get a room by ID", description = "Retrieves a room by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of room",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "404", description = "Room not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@Parameter(description = "ID of the room to retrieve") @PathVariable Long id) {
        Room room;
        try{
            room = roomService.getRoomById(id);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @Operation(summary = "Update a room", description = "Updates an existing room")
    @ApiResponse(responseCode = "200", description = "Room updated successfully",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "404", description = "Room not found")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@Parameter(description = "ID of the room to update")
                                            @PathVariable Long id,
                                        @Parameter(description = "Updated room details") @RequestBody Room roomDetails) {
        try {
            Room updatedRoom = roomService.updateRoom(id, roomDetails);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }// catch (Exception e) {
          //  return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
        //}
    }

    @Operation(summary = "Delete a room", description = "Deletes an existing room")
    @ApiResponse(responseCode = "204", description = "Room deleted successfully")
    @ApiResponse(responseCode = "404", description = "Room not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@Parameter(description = "ID of the room to delete") @PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get devices by room ID", description = "Retrieves all devices for a specific room")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of devices",
            content = @Content(schema = @Schema(implementation = Device.class)))
    @ApiResponse(responseCode = "404", description = "Room not found")
    @GetMapping("/{roomId}/devices")
    public ResponseEntity<?> getDevicesByRoomId(@Parameter(description = "ID of the room to retrieve devices for")
                                                    @PathVariable Long roomId) {
        try {
            List<Device> devices = roomService.getDevicesByRoomId(roomId);
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("Invalid input data", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}