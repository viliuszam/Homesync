package me.vilius.homesync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import me.vilius.homesync.model.Home;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.model.dto.HomeDTO;
import me.vilius.homesync.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/homes")
@Tag(name = "Home", description = "Home management APIs")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;

    @Operation(summary = "Create a new home", description = "Creates a new home")
    @ApiResponse(responseCode = "201", description = "Home created successfully",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "400", description = "Malformed request")
    @ApiResponse(responseCode = "422", description = "Invalid payload data")
    @PostMapping
    public ResponseEntity<?> createHome(@Parameter(description = "Home details")
                                        @Valid @RequestBody HomeDTO homeDTO) {
        try {
            Home createdHome = homeService.createHome(homeDTO);
            return new ResponseEntity<>(createdHome, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Get all homes", description = "Retrieves a list of all homes")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of homes",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @GetMapping
    public ResponseEntity<List<Home>> getAllHomes() {
        return new ResponseEntity<>(homeService.getAllHomes(), HttpStatus.OK);
    }

    @Operation(summary = "Get a home by ID", description = "Retrieves a home by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of home",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getHomeById(@Parameter(description = "ID of the home to retrieve") @PathVariable Long id) {
        Home home;
        try{
            home = homeService.getHomeById(id);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(home, HttpStatus.OK);
    }

    @Operation(summary = "Update a home", description = "Updates an existing home")
    @ApiResponse(responseCode = "200", description = "Home updated successfully",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @ApiResponse(responseCode = "422", description = "Invalid payload data")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHome(@Parameter(description = "ID of the home to update")
                                        @PathVariable Long id,
                                        @Valid @RequestBody HomeDTO homeDTO) {
        try {
            Home updatedHome = homeService.updateHome(id, homeDTO);
            return new ResponseEntity<>(updatedHome, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a home", description = "Deletes an existing home")
    @ApiResponse(responseCode = "204", description = "Home deleted successfully")
    @ApiResponse(responseCode = "404", description = "Home not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHome(@Parameter(description = "ID of the home to delete")
                                            @PathVariable Long id) {
        try {
            homeService.deleteHome(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get rooms by home ID", description = "Retrieves all rooms for a specific home")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of rooms",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @GetMapping("/{homeId}/rooms")
    public ResponseEntity<?> getRoomsByHomeId(@Parameter(description = "ID of the home to retrieve rooms for")
                                                  @PathVariable Long homeId) {
        try {
            List<Room> rooms = homeService.getRoomsByHomeId(homeId);
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
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