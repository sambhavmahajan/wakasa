package com.github.sambhavmahajan.wakasa.repository;

import com.github.sambhavmahajan.wakasa.entity.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AppToken, String> {
    Optional<AppToken> findAppTokenByValue(String value);
    Optional<AppToken> findByOwnerEmail(String ownerEmail);
}
