package com.grocify.backend.controller;

import com.grocify.backend.entity.User;
import com.grocify.backend.service.UserService;
import com.grocify.backend.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
}