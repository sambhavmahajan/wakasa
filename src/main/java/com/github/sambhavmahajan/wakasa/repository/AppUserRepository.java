package com.github.sambhavmahajan.wakasa.repository;

import com.github.sambhavmahajan.wakasa.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
}
