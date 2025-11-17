package com.slotsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SlotSync - Sistema SaaS de Gestión de Reservas
 *
 * @author SlotSync Team
 * @version 1.0.0
 */
@SpringBootApplication(exclude = {
    RedisAutoConfiguration.class,
    RedisRepositoriesAutoConfiguration.class
})
@EnableJpaAuditing
// @EnableCaching  // Temporarily disabled - enable when Redis is configured
@EnableAsync
@EnableScheduling
public class SlotSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlotSyncApplication.class, args);
    }
}
