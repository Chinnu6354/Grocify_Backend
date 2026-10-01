package com.grocify.backend.service;

import com.grocify.backend.entity.Otp;
import com.grocify.backend.repository.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class OtpService {

    private static final Logger log =
            LoggerFactory.getLogger(OtpService.class);

    private static final String SENDER_EMAIL =
            "sonupradhan6354@gmail.com";

    private final OtpRepository otpRepository;
    private final RestClient brevoClient;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;

        String apiKey = System.getenv("BREVO_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "BREVO_API_KEY environment variable is missing"
            );
        }

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(Duration.ofSeconds(20));

        this.brevoClient = RestClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .requestFactory(requestFactory)
                .defaultHeader("api-key", apiKey.trim())
                .build();
    }

    // GENERATE + SEND + SAVE OTP
    public void generateAndSaveOtp(String email) {
        String otpCode = String.format(
                Locale.ROOT,
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        String htmlContent =
                "<h2>Grocify Email Verification</h2>" +
                        "<p>Your verification OTP is:</p>" +
                        "<h1>" + otpCode + "</h1>" +
                        "<p>This OTP is valid for 5 minutes.</p>" +
                        "<p>Please do not share this OTP with anyone.</p>" +
                        "<br><p>Thank you,<br>Grocify Team</p>";

        Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                        "name", "Grocify",
                        "email", SENDER_EMAIL
                ),
                "to", List.of(Map.of("email", email)),
                "subject", "Grocify - Email Verification OTP",
                "htmlContent", htmlContent
        );

        try {
            brevoClient.post()
                    .uri("/smtp/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException e) {
            // Temporary diagnostics: redact the API key and OTP before logging.
            String safeError = e.getResponseBodyAsString()
                    .replace(System.getenv("BREVO_API_KEY").trim(), "[REDACTED]")
                    .replace(otpCode, "[REDACTED]")
                    .replace('\r', ' ')
                    .replace('\n', ' ');

            log.error(
                    "Brevo rejected OTP request. HTTP {}: {}",
                    e.getStatusCode().value(),
                    safeError
            );

            throw new IllegalStateException(
                    "Unable to send verification email. Please try again later."
            );

        } catch (RestClientException e) {
            log.error("Unable to reach Brevo to send OTP email.");

            throw new IllegalStateException(
                    "Email service is temporarily unavailable. Please try again later."
            );
        }

        // Save only after Brevo accepts the email request.
        // Acceptance does not guarantee inbox delivery.
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(otpCode);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);

        otpRepository.save(otp);

        log.info("Brevo accepted OTP email request and OTP was saved.");
    }

    // VERIFY OTP
    public boolean verifyOtp(String email, String enteredOtp) {
        Otp otp = otpRepository
                .findTopByEmailOrderByIdDesc(email)
                .orElse(null);

        if (otp == null) {
            return false;
        }

        if (otp.isVerified()) {
            return false;
        }

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            return false;
        }

        if (!otp.getOtp().equals(enteredOtp)) {
            return false;
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        return true;
    }
}