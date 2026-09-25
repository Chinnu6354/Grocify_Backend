package com.grocify.backend.service;

import com.grocify.backend.entity.Otp;
import com.grocify.backend.repository.OtpRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

import jakarta.annotation.PostConstruct;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    public OtpService(
            OtpRepository otpRepository,
            JavaMailSender mailSender
    ) {
        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
    }




    @PostConstruct
    public void checkMailConfiguration() {

        System.out.println("=================================");
        System.out.println("MAIL USERNAME: "
                + System.getenv("MAIL_USERNAME"));

        System.out.println("MAIL PASSWORD PRESENT: "
                + (System.getenv("MAIL_PASSWORD") != null
                && !System.getenv("MAIL_PASSWORD").isBlank()));

        System.out.println("=================================");
    }





    // =========================
    // GENERATE + SAVE + SEND OTP
    // =========================

    public void generateAndSaveOtp(String email) {

        // Generate 6-digit OTP
        String otpCode = String.format(
                "%06d",
                new Random().nextInt(1000000)
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

        // Save OTP in database
        otpRepository.save(otp);

        // =========================
        // SEND OTP TO USER EMAIL
        // =========================

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Grocify - Email Verification OTP"
        );

        message.setText(
                "Hello,\n\n" +
                        "Your Grocify verification OTP is:\n\n" +
                        otpCode +
                        "\n\n" +
                        "This OTP is valid for 5 minutes.\n\n" +
                        "Please do not share this OTP with anyone.\n\n" +
                        "Thank you,\n" +
                        "Grocify Team"
        );

        mailSender.send(message);

        System.out.println(
                "OTP sent successfully to: " + email
        );
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