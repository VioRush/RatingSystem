package com.leverx.training.RatingSystem.controller;

import com.leverx.training.RatingSystem.db.dto.LoginRequest;
import com.leverx.training.RatingSystem.db.dto.UserCreationDTO;
import com.leverx.training.RatingSystem.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserCreationDTO userDTO){
        userDTO.setCreatedAt(Instant.now());
        try{
            authenticationService.signUp(userDTO);
            return ResponseEntity.ok("User have been created! Link for email confirmation was sent to provided email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token){
        try {
            authenticationService.verifyEmail(token);
            return ResponseEntity.ok("Email is successfully confirmed!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            String token = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
