package com.github.sambhavmahajan.wakasa.service;

import com.github.sambhavmahajan.wakasa.entity.BloomEntity;
import com.github.sambhavmahajan.wakasa.repository.BloomFilterRepository;
import com.google.common.hash.BloomFilter;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BloomFilterService {
    private final BloomFilter<String> filter;
    private final BloomFilterRepository repository;
    public boolean mightContain(String username) {
        return filter.mightContain(username);
    }
    @PostConstruct
    private void initFilter() {
        Optional<BloomEntity> e = repository.findById(0);
        BloomEntity entity;
        if(e.isEmpty()) {
            entity = new BloomEntity();
            entity.setId(0);
            entity.setData(SerializationUtils.serialize(filter));
            repository.save(entity);
        } else {
            BloomFilter<String> ret = (BloomFilter<String>) SerializationUtils.deserialize(e.get().getData());
            if(ret != null) filter.putAll(ret);
        }
    }
    @Transactional
    public void save(@NonNull String username) {
        Optional<BloomEntity> e = repository.findById(0);
        BloomEntity bloom;
        if(e.isEmpty()) {
            bloom = new BloomEntity();
            bloom.setId(0);
        } else {
            bloom = e.get();
        }
        filter.put(username);
        bloom.setData(SerializationUtils.serialize(filter));
    }
}
