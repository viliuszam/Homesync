package me.vilius.homesync.controller;

import me.vilius.homesync.model.CustomUserDetails;
import me.vilius.homesync.model.Role;
import me.vilius.homesync.model.User;
import me.vilius.homesync.model.dto.AuthRequest;
import me.vilius.homesync.model.dto.AuthResponse;
import me.vilius.homesync.model.dto.TokenRefreshRequest;
import me.vilius.homesync.repository.UserRepository;
import me.vilius.homesync.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest registerRequest) {
        if(registerRequest.getUsername() == null){
            return new ResponseEntity<>("Username must be provided.", HttpStatus.BAD_REQUEST);
        }

        if(registerRequest.getPassword() == null){
            return new ResponseEntity<>("Password must be provided.", HttpStatus.BAD_REQUEST);
        }

        if(registerRequest.getUsername().length() < 3 || registerRequest.getUsername().length() > 12){
            return new ResponseEntity<>("Username must be between 3 and 12 characters long.", HttpStatus.BAD_REQUEST);
        }

        if(registerRequest.getPassword().length() < 8){
            return new ResponseEntity<>("Password must be at least 8 characters long.", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(Role.MEMBER);

        try {
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Failed to register user", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        if(authRequest.getUsername() == null){
            return new ResponseEntity<>("Username must be provided.", HttpStatus.BAD_REQUEST);
        }

        if(authRequest.getPassword() == null){
            return new ResponseEntity<>("Password must be provided.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renewToken(@RequestBody TokenRefreshRequest request) {
        if (jwtUtil.validateJwtToken(request.getToken())) {
            String username = jwtUtil.getUsernameFromJwtToken(request.getToken());
            User user = userRepository.findByUsername(username).orElseThrow();
            String newToken = jwtUtil.generateToken(username, user.getRole());
            return ResponseEntity.ok(new AuthResponse(newToken));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
        }
    }
}