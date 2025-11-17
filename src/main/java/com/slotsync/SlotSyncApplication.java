package com.slotsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class SlotSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlotSyncApplication.class, args);
    }
}
