package com.github.sambhavmahajan.wakasa.service;

import com.github.sambhavmahajan.wakasa.entity.AppToken;
import com.github.sambhavmahajan.wakasa.exception.InvalidPayloadException;
import com.github.sambhavmahajan.wakasa.exception.InvalidTokenException;
import com.github.sambhavmahajan.wakasa.payload.EmailPayload;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService {
    private TokenService tokenService;
    public void emailPayloadValid(EmailPayload payload) {
        if(payload.key() == null || payload.key().isEmpty()) {
            throw new InvalidPayloadException("Key is either null or empty");
        }
        if(payload.message() == null || payload.message().isEmpty()) {
            throw new InvalidPayloadException("Message is either null or empty");
        }
    }
    @Retryable(maxAttempts = 4, backoff = @Backoff(5000))
    public void sendEmail(EmailPayload payload) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        emailPayloadValid(payload);
        Optional<AppToken> oToken = tokenService.getTokenByKey(payload.key());
        if(oToken.isEmpty()) throw new InvalidTokenException("Invalid token");
        AppToken token = oToken.get();
        mailSender.setHost(token.getHost());
        mailSender.setPassword(token.getPassword());
        mailSender.setPort(token.getPort());
        mailSender.setUsername(token.getSmtpUsername());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(token.getFromEmail());
        message.setTo(payload.to());
        message.setSubject(payload.subject());
        message.setText(payload.message());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        mailSender.send(message);
    }
}
