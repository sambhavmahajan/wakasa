package com.github.sambhavmahajan.wakasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableRetry
public class WakasaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WakasaApplication.class, args);
    }

}
