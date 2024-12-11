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
import me.vilius.homesync.model.dto.HomeDTO;
import me.vilius.homesync.repository.UserRepository;
import me.vilius.homesync.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/homes")
@Tag(name = "Home", description = "Home management APIs")
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Create a new home", description = "Creates a new home")
    @ApiResponse(responseCode = "201", description = "Home created successfully",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "400", description = "Malformed request")
    @ApiResponse(responseCode = "422", description = "Invalid payload data")
    @PostMapping
    public ResponseEntity<?> createHome(@Valid @RequestBody HomeDTO homeDTO, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try{
            Home createdHome = homeService.createHome(homeDTO, user);
            return new ResponseEntity<>(createdHome, HttpStatus.CREATED);
        }catch(IllegalArgumentException iae){
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Get all homes", description = "Retrieves a list of all homes of the requesting user.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of homes.",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @GetMapping
    public ResponseEntity<List<Home>> getUserHomes(Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        return new ResponseEntity<>(homeService.getUserHomes(user), HttpStatus.OK);
    }

    @Operation(summary = "Get a home by ID", description = "Retrieves a home by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of home",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @GetMapping("/{id}")
    public ResponseEntity<?> getHomeById(@PathVariable Long id, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Home home = homeService.getHomeById(id);
            if(user.getRole().equals(Role.ADMINISTRATOR)
                    || home.getUser().getId().equals(user.getId())){

                return new ResponseEntity<>(home, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Home not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get homes by username", description = "Gets users homes by username (ADMIN only)")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of homes",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getHomesByUsername(@PathVariable String name, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if(!user.getRole().equals(Role.ADMINISTRATOR)){
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        var homeOwner = userRepository.findByUsername(name);
        if(!homeOwner.isPresent()){
            return new ResponseEntity<>("User not found.", HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<>(homeService.getUserHomes(homeOwner.get()), HttpStatus.OK);
        }
    }

    @Operation(summary = "Update a home", description = "Updates an existing home")
    @ApiResponse(responseCode = "200", description = "Home updated successfully",
            content = @Content(schema = @Schema(implementation = Home.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @ApiResponse(responseCode = "422", description = "Invalid payload data")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHome(@Parameter(description = "ID of the home to update")
                                        @PathVariable Long id
            , @Valid @RequestBody HomeDTO homeDTO, Authentication authentication) {

        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            if(user.getId().equals(id) || user.getRole().equals(Role.ADMINISTRATOR)){
                Home updatedHome = homeService.updateHome(id, homeDTO, user);
                return new ResponseEntity<>(updatedHome, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
            }
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
                                            @PathVariable Long id, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            if(user.getId().equals(id) || user.getRole().equals(Role.ADMINISTRATOR)){
                homeService.deleteHome(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Home not found or unauthorized access", HttpStatus.NOT_FOUND);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Get rooms by home ID", description = "Retrieves all rooms for a specific home")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of rooms",
            content = @Content(schema = @Schema(implementation = Room.class)))
    @ApiResponse(responseCode = "404", description = "Home not found")
    @GetMapping("/{homeId}/rooms")
    public ResponseEntity<?> getRoomsByHomeId(@Parameter(description = "ID of the home to retrieve rooms for")
                                                  @PathVariable Long homeId, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Home home = homeService.getHomeById(homeId);
            if(user.getId().equals(home.getUser().getId()) || user.getRole().equals(Role.ADMINISTRATOR)) {
                List<Room> rooms = home.getRooms();
                return new ResponseEntity<>(rooms, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Home not found or unauthorized access", HttpStatus.NOT_FOUND);
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