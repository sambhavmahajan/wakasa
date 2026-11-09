package com.github.sambhavmahajan.wakasa.repository;

import com.github.sambhavmahajan.wakasa.entity.BloomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloomFilterRepository extends JpaRepository<BloomEntity, Integer> {
}
