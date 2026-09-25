package com.grocify.backend.controller;

import com.grocify.backend.entity.User;
import com.grocify.backend.repository.UserRepository;
import com.grocify.backend.service.JwtService;
import com.grocify.backend.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class OtpController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public OtpController(
            OtpService otpService,
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // SEND OTP
    // =========================

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
            @RequestBody Map<String, String> request
    ) {

        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }

        otpService.generateAndSaveOtp(email);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully"
                )
        );
    }

    // =========================
// SIGNUP - SEND OTP
// =========================

    @PostMapping("/signup/send-otp")
    public ResponseEntity<?> signupSendOtp(
            @RequestBody Map<String, String> request
    ) {

        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }

        // Check whether user already exists
        if (userRepository.findByEmail(email).isPresent()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is already registered"
                    ));
        }

        // Generate OTP
        otpService.generateAndSaveOtp(email);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully"
                )
        );
    }

    // =========================
// SIGNUP - VERIFY OTP
// =========================

    @PostMapping("/signup/verify-otp")
    public ResponseEntity<?> signupVerifyOtp(
            @RequestBody Map<String, String> request
    ) {

        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        String otp = request.get("otp");


        // =========================
        // VALIDATION
        // =========================

        if (name == null || name.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Name is required"
                    ));
        }

        if (email == null || email.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }

        if (password == null || password.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Password is required"
                    ));
        }

        if (otp == null || otp.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "OTP is required"
                    ));
        }


        // =========================
        // CHECK USER
        // =========================

        if (userRepository.findByEmail(email).isPresent()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is already registered"
                    ));
        }


        // =========================
        // VERIFY OTP
        // =========================

        boolean verified =
                otpService.verifyOtp(email, otp);


        if (!verified) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid or expired OTP"
                    ));
        }


        // =========================
        // CREATE USER
        // =========================

        User user = new User();

        user.setName(name);
        user.setEmail(email);

        // We will fix password encryption in the next step
        user.setPassword(
                passwordEncoder.encode(password)
        );

        // Normal customer
        user.setRole("USER");


        // =========================
        // SAVE USER
        // =========================

        userRepository.save(user);


        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Signup successful",
                        "email",
                        user.getEmail()
                )
        );
    }


    // =========================
    // VERIFY OTP
    // =========================

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody Map<String, String> request
    ) {

        String email = request.get("email");
        String otp = request.get("otp");


        // Validate email

        if (email == null || email.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }


        // Validate OTP

        if (otp == null || otp.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "OTP is required"
                    ));
        }


        // Verify OTP

        boolean verified =
                otpService.verifyOtp(email, otp);


        if (!verified) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid or expired OTP"
                    ));
        }


        // =========================
        // FIND USER
        // =========================

        User user = userRepository
                .findByEmail(email)
                .orElse(null);


        if (user == null) {

            return ResponseEntity.status(404)
                    .body(Map.of(
                            "message",
                            "User not found"
                    ));
        }


        // =========================
        // GET USER ROLE
        // =========================

        String role = user.getRole();


        // =========================
        // GENERATE JWT
        // =========================

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        role
                );


        // =========================
        // RETURN JWT
        // =========================

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP verified successfully",
                        "token",
                        token,
                        "email",
                        user.getEmail(),
                        "role",
                        role
                )
        );
    }

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<?> sendForgotPasswordOtp(
            @RequestBody Map<String, String> request
    ) {

        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }

        // Check whether user exists
        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {

            return ResponseEntity.status(404)
                    .body(Map.of(
                            "message",
                            "User not found"
                    ));
        }

        // Generate and send OTP
        otpService.generateAndSaveOtp(email);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully"
                )
        );
    }
    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<?> verifyForgotPasswordOtp(
            @RequestBody Map<String, String> request
    ) {

        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }

        if (otp == null || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "OTP is required"
                    ));
        }

        boolean verified =
                otpService.verifyOtp(email, otp);

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid or expired OTP"
                    ));
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP verified successfully"
                )
        );
    }
    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> request
    ) {

        String email = request.get("email");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required"
                    ));
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "New password is required"
                    ));
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Confirm password is required"
                    ));
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Passwords do not match"
                    ));
        }

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404)
                    .body(Map.of(
                            "message",
                            "User not found"
                    ));
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password reset successfully"
                )
        );
    }
}