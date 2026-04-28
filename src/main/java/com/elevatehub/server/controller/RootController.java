package com.elevatehub.server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class RootController {

    @GetMapping("/")
    public Map<String, String> healthCheck() {
        return Map.of(
            "status", "UP",
            "message", "ElevateHub Backend is running",
            "api_docs", "/api/mentors"
        );
    }
}
