package com.github.sambhavmahajan.wakasa.service;

import com.github.sambhavmahajan.wakasa.dto.TokenDTO;
import com.github.sambhavmahajan.wakasa.entity.AppToken;
import com.github.sambhavmahajan.wakasa.entity.AppUser;
import com.github.sambhavmahajan.wakasa.exception.TokenAlreadyExistsException;
import com.github.sambhavmahajan.wakasa.exception.TokenNotFoundException;
import com.github.sambhavmahajan.wakasa.repository.TokenRepository;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository repository;
    public Optional<AppToken> getTokenByKey(String key) {
        return repository.findAppTokenByValue(key);
    }
    @Cacheable(cacheNames="tokens", key="#principal.getName()", sync = true)
    public AppToken getTokenByUser(Principal principal) {
        Optional<AppToken> tokn = repository.findByOwnerEmail(principal.getName());
        if(tokn.isEmpty()) {
            throw new TokenNotFoundException("Token not found for user");
        }
        return tokn.get();
    }
    @CachePut(cacheNames="tokens", key="#token.getOwner().getEmail()")
    @Transactional
    public AppToken save(AppToken token) {
        return repository.save(token);
    }

    @CachePut(cacheNames="tokens", key="#principal.getName()")
    @Transactional
    public AppToken modifyToken(Principal principal, TokenDTO tokenDTO) {
        AppToken token = repository.findByOwnerEmail(principal.getName()).orElseThrow();
        token.setPassword(tokenDTO.password());
        token.setNewValue();
        token.setPort(tokenDTO.port());
        token.setHost(tokenDTO.host());
        token.setSmtpUsername(tokenDTO.smtpUsername());
        token.setFromEmail(tokenDTO.fromEmail());
        return repository.save(token);
    }
}
