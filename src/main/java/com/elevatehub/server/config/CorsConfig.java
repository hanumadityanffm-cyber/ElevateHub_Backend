package com.elevatehub.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS Configuration for ElevateHub.
 *
 * This config works in tandem with @CrossOrigin on individual controllers
 * to provide a "belt-and-suspenders" CORS strategy:
 *   • This class handles the global defaults.
 *   • @CrossOrigin on controllers provides per-endpoint visibility.
 *
 * Allowed origins are externalised so they can be overridden for production
 * via application.properties without a code change.
 */
@Configuration
public class CorsConfig {

    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:3000}")
    private String[] allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")                          // Every endpoint
                        .allowedOrigins(allowedOrigins)              // React dev servers
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true)
                        .maxAge(3600);                               // Pre-flight cache 1 hour
            }
        };
    }
}
