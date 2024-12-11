package me.vilius.homesync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import me.vilius.homesync.model.*;
import me.vilius.homesync.model.dto.RoomDTO;
import me.vilius.homesync.service.HomeService;
import me.vilius.homesync.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Room", description = "Room management APIs")
public class RoomController extends BaseController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private HomeService homeService;

    @Operation(summary = "Create a new room", description = "Creates a new room in a specific home")
    @ApiResponse(responseCode = "201", description = "Room created successfully",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Home not found")
    @ApiResponse(responseCode = "422", description = "Unprocessable entity")
    @PostMapping
    public ResponseEntity<?> createRoom(
            @Parameter(description = "ID of the home to add the room to") @RequestParam Long homeId,
            @Parameter(description = "Room details") @Valid @RequestBody RoomDTO roomDTO, Authentication authentication) {

        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if (!homeService.userOwnsHome(user.getId(), homeId) && !user.getRole().equals(Role.ADMINISTRATOR)) {
            return new ResponseEntity<>("Unauthorized access to home", HttpStatus.FORBIDDEN);
        }

        try {
            Room createdRoom = roomService.createRoom(homeId, roomDTO);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @Operation(summary = "Get all rooms", description = "Retrieves a list of all rooms")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of rooms",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        List<Room> userRooms = roomService.getRoomsByUserHomes(user.getId());
        return new ResponseEntity<>(userRooms, HttpStatus.OK);
    }

    @Operation(summary = "Get a room by ID", description = "Retrieves a room by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of room",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "404", description = "Room not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@Parameter(description = "ID of the room to retrieve") @PathVariable Long id
            , Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        Room room;
        try {
            room = roomService.getRoomById(id);
            if (!homeService.userOwnsHome(user.getId(), room.getHome().getId()) && !user.getRole().equals(Role.ADMINISTRATOR)) {
                return new ResponseEntity<>("Unauthorized access to room", HttpStatus.FORBIDDEN);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @Operation(summary = "Update a room", description = "Updates an existing room")
    @ApiResponse(responseCode = "200", description = "Room updated successfully",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Room not found")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(
            @Parameter(description = "ID of the room to update") @PathVariable Long id,
            @Parameter(description = "Updated room details") @Valid @RequestBody RoomDTO roomDTO,
            Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Room room = roomService.getRoomById(id);
            if (!homeService.userOwnsHome(user.getId(), room.getHome().getId()) && !user.getRole().equals(Role.ADMINISTRATOR)) {
                return new ResponseEntity<>("Unauthorized access to room", HttpStatus.FORBIDDEN);
            }
            Room updatedRoom = roomService.updateRoom(id, roomDTO);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Delete a room", description = "Deletes an existing room")
    @ApiResponse(responseCode = "204", description = "Room deleted successfully")
    @ApiResponse(responseCode = "404", description = "Room not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@Parameter(description = "ID of the room to delete") @PathVariable Long id,
                                        Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Room room = roomService.getRoomById(id);
            if (!homeService.userOwnsHome(user.getId(), room.getHome().getId()) && !user.getRole().equals(Role.ADMINISTRATOR)) {
                return new ResponseEntity<>("Unauthorized access to room", HttpStatus.FORBIDDEN);
            }
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
                                                    @PathVariable Long roomId, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Room room = roomService.getRoomById(roomId);
            if (!homeService.userOwnsHome(user.getId(), room.getHome().getId()) && !user.getRole().equals(Role.ADMINISTRATOR)) {
                return new ResponseEntity<>("Unauthorized access to room", HttpStatus.FORBIDDEN);
            }
            List<Device> devices = roomService.getDevicesByRoomId(roomId);
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
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