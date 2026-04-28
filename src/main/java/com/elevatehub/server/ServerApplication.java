package com.elevatehub.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.elevatehub.server")
public class ServerApplication {

    public static void main(String[] args) {
        System.out.println("\n--- STARTING ELEVATEHUB SERVER ---");
        SpringApplication.run(ServerApplication.class, args);
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("  ElevateHub Server is running on port 8080");
        System.out.println("══════════════════════════════════════════════\n");
    }
}
