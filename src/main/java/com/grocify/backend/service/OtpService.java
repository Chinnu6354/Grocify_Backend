package com.grocify.backend.service;

import com.grocify.backend.entity.Otp;
import com.grocify.backend.repository.OtpRepository;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final Resend resend;

    public OtpService(OtpRepository otpRepository) {

        this.otpRepository = otpRepository;

        String apiKey = System.getenv("RESEND_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "RESEND_API_KEY environment variable is missing"
            );
        }

        this.resend = new Resend(apiKey);
    }

    // =========================
    // GENERATE + SEND + SAVE OTP
    // =========================

    public void generateAndSaveOtp(String email) {

        // Generate secure 6-digit OTP
        String otpCode = String.format(
                "%06d",
                new SecureRandom().nextInt(1000000)
        );

        // OTP expires after 5 minutes
        LocalDateTime expiresAt =
                LocalDateTime.now().plusMinutes(5);

        // Create OTP entity
        Otp otp = new Otp();

        otp.setEmail(email);
        otp.setOtp(otpCode);
        otp.setExpiresAt(expiresAt);
        otp.setVerified(false);

        // =========================
        // SEND OTP USING RESEND
        // =========================

        CreateEmailOptions emailOptions =
                CreateEmailOptions.builder()
                        .from("onboarding@resend.dev")
                        .to(email)
                        .subject("Grocify - Email Verification OTP")
                        .html(
                                "<h2>Grocify Email Verification</h2>" +
                                        "<p>Your verification OTP is:</p>" +
                                        "<h1>" + otpCode + "</h1>" +
                                        "<p>This OTP is valid for 5 minutes.</p>" +
                                        "<p>Please do not share this OTP with anyone.</p>" +
                                        "<br>" +
                                        "<p>Thank you,<br>Grocify Team</p>"
                        )
                        .build();

        try {

            var response =
                    resend.emails().send(emailOptions);

            // Save OTP only after email is successfully sent
            otpRepository.save(otp);

            System.out.println(
                    "OTP email sent successfully. Email ID: "
                            + response.getId()
            );

        } catch (ResendException e) {

            System.out.println(
                    "Failed to send OTP email: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Unable to send OTP email",
                    e
            );
        }
    }

    // =========================
    // VERIFY OTP
    // =========================

    public boolean verifyOtp(
            String email,
            String enteredOtp
    ) {

        Otp otp = otpRepository
                .findTopByEmailOrderByIdDesc(email)
                .orElse(null);

        // OTP not found
        if (otp == null) {
            return false;
        }

        // OTP already used
        if (otp.isVerified()) {
            return false;
        }

        // OTP expired
        if (LocalDateTime.now()
                .isAfter(otp.getExpiresAt())) {

            return false;
        }

        // OTP doesn't match
        if (!otp.getOtp().equals(enteredOtp)) {
            return false;
        }

        // OTP is valid
        otp.setVerified(true);

        otpRepository.save(otp);

        return true;
    }
}