package com.museumfinder;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@ConfigurationPropertiesScan
public class AppConfig {

    /** Injected everywhere a "now" is needed, so opening-hour logic is testable. */
    @Bean
    Clock clock() {
        return Clock.system(java.time.ZoneId.of("Europe/Helsinki"));
    }
}
