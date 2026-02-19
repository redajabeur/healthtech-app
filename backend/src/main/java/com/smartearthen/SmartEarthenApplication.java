package com.smartearthen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SmartEarthenApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartEarthenApplication.class, args);
        System.out.println("""
            ╔══════════════════════════════════════════╗
            ║   🏛️  Smart Earthen Buildings — Online    ║
            ║   IoT + AI Monitoring System              ║
            ║   API: http://localhost:8080              ║
            ╚══════════════════════════════════════════╝
            """);
    }
}
