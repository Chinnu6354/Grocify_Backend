package com.grocify.backend.controller;

import com.grocify.backend.entity.User;
import com.grocify.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.grocify.backend.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) {

        System.out.println("AUTH CONTROLLER LOGIN CALLED");
        System.out.println("LOGIN EMAIL: " + user.getEmail());

        return userService.login(
                user.getEmail(),
                user.getPassword()
        );
    }
}