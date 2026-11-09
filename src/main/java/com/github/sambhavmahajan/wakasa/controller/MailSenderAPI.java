package com.github.sambhavmahajan.wakasa.controller;

import com.github.sambhavmahajan.wakasa.exception.InvalidPayloadException;
import com.github.sambhavmahajan.wakasa.exception.InvalidTokenException;
import com.github.sambhavmahajan.wakasa.payload.EmailPayload;
import com.github.sambhavmahajan.wakasa.service.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api"))
@AllArgsConstructor
public class MailSenderAPI {
    private EmailService emailService;
    @PostMapping("/V1")
    public ResponseEntity<String> apiVersion1(@RequestBody @Valid EmailPayload emailPayload) {
        try {
            emailService.sendEmail(emailPayload);
        } catch(InvalidTokenException | InvalidPayloadException ex) {
            return ResponseEntity.status(400).
                    body("Failed to send email, kindly check your payload fields are correct and non empty");
        } catch (Exception ex) {
            return ResponseEntity.status(500).
                    body("Failed to send email, kindly recheck your SMTP configuration");
        }
        return ResponseEntity.ok("Email sent");
    }
}
