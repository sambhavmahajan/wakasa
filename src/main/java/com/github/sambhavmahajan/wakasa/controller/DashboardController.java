package com.github.sambhavmahajan.wakasa.controller;

import com.github.sambhavmahajan.wakasa.dto.TokenDTO;
import com.github.sambhavmahajan.wakasa.entity.AppToken;
import com.github.sambhavmahajan.wakasa.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {
    private final TokenService tokenService;
    @GetMapping
    public String dashboard(Model model, Principal principal) {
        AppToken token = tokenService.getTokenByUser(principal);
        TokenDTO tokenDto = new TokenDTO(token.getValue(), token.getSmtpUsername(), token.getHost(), token.getPort(), token.getPassword(), token.getFromEmail());
        model.addAttribute("tokenDto", tokenDto);
        return "dashboard";
    }
    @PostMapping
    public ResponseEntity<String> createToken(@RequestBody TokenDTO tokenDTO, Principal principal) {
        try {
            tokenService.modifyToken(principal, tokenDTO);
        } catch(RuntimeException ex) {
            return ResponseEntity.status(500).body("Internal Server Error, Please try again!");
        }
        return ResponseEntity.ok("Saved, kindly refresh to copy the token");
    }
}
