package com.museumfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MuseumFinderApplication {

    public static void main(String[] args) {
        // Hosting platforms supply the database as a single DATABASE_URL; translate it
        // before Spring reads its configuration.
        DatabaseUrl.applyIfPresent();
        SpringApplication.run(MuseumFinderApplication.class, args);
    }
}
