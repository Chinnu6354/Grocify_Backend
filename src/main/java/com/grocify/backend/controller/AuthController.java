package com.grocify.backend.controller;

import com.grocify.backend.entity.User;
import com.grocify.backend.service.UserService;
import com.grocify.backend.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocify.backend.service.OtpService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;

    public AuthController(UserService userService,OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("AUTH TEST ENDPOINT CALLED");
        return "AUTH API WORKING";
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        System.out.println("AUTH CONTROLLER LOGIN CALLED");
        System.out.println("LOGIN EMAIL: " + user.getEmail());

        try {

            LoginResponse response = userService.login(
                    user.getEmail(),
                    user.getPassword()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            System.out.println("LOGIN ERROR: " + e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/signup/send-otp")
    public ResponseEntity<?> sendSignupOtp(
            @RequestBody User user
    ) {

        try {

            if (userService.emailExists(user.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body("Email already registered");
            }

            otpService.generateAndSaveOtp(user.getEmail());

            return ResponseEntity.ok(
                    "OTP sent successfully"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP");
        }
    }
}