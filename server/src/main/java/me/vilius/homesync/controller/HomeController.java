package me.vilius.homesync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Home", description = "Home management APIs")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Operation(summary = "Create a new home", description = "Creates a new home")
    @ApiResponse(responseCode = "201", description = "Home created successfully",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "422", description = "Invalid payload")
    @PostMapping
    public ResponseEntity<?> createHome(@Parameter(description = "Home details")
                                            @RequestBody Home home) {
        try {
            Home createdHome = homeService.createHome(home);
            return new ResponseEntity<>(createdHome, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid payload", HttpStatus.UNPROCESSABLE_ENTITY);
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
            Home home = homeService.getHomeById(id);
            if(home != null){
                return new ResponseEntity<>(home, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
            }
    }

    @Operation(summary = "Update a home", description = "Updates an existing home")
    @ApiResponse(responseCode = "200", description = "Home updated successfully",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHome(@Parameter(description = "ID of the home to update") @PathVariable Long id,
                                        @Parameter(description = "Updated home details") @RequestBody Home homeDetails) {
        try {
            Home updatedHome = homeService.updateHome(id, homeDetails);
            return new ResponseEntity<>(updatedHome, HttpStatus.OK);
        } catch (Exception e) {
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
}