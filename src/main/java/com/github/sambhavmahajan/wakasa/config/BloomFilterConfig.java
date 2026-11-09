package com.github.sambhavmahajan.wakasa.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class BloomFilterConfig {
    @Bean
    public BloomFilter<String> bloomFilter() {
        int expectedInsertions = 100000;
        double falsePositive = 0.01;
        return BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                        expectedInsertions,
                        falsePositive);
    }
}
