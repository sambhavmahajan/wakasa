package com.github.sambhavmahajan.wakasa.controller;

import com.github.sambhavmahajan.wakasa.dto.RegisterDTO;
import com.github.sambhavmahajan.wakasa.exception.BadPasswordException;
import com.github.sambhavmahajan.wakasa.exception.EmailAlreadyExistsException;
import com.github.sambhavmahajan.wakasa.exception.InvalidEmailFormatException;
import com.github.sambhavmahajan.wakasa.exception.PasswordMissmatchException;
import com.github.sambhavmahajan.wakasa.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.Semaphore;

@Controller
@AllArgsConstructor
public class AuthController {
    private final AppUserService userService;
    private final Semaphore semaphore = new Semaphore(100, true);
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDto", new RegisterDTO("", "", ""));
        return "register";
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerAPI(@RequestBody RegisterDTO register) {
        boolean acquiredLock = false;
        try {
            if(!semaphore.tryAcquire()) {
                return ResponseEntity.status(429).body("Too many requests");
            }
            acquiredLock = true;
            userService.createNewUser(register);
        } catch(PasswordMissmatchException ex) {
            return ResponseEntity.status(400).body("Passwords do not match");
        } catch(EmailAlreadyExistsException ex) {
            return ResponseEntity.status(400).body(register.email() + " already exists, please login or make new account");
        } catch(BadPasswordException | InvalidEmailFormatException ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        } catch(RuntimeException ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        } finally {
            if(acquiredLock) semaphore.release();
        }
        return ResponseEntity.status(201).body("Kindly login to " + register.email() + ", registration successful");
    }
}
