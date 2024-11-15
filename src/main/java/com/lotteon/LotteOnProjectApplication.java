package com.lotteon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.lotteon.repository")
public class LotteOnProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteOnProjectApplication.class, args);
    }

}
