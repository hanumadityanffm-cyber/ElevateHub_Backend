package com.elevatehub.server.controller;

import com.elevatehub.server.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        System.out.println(">>> RECEIVED LOGIN REQUEST for: " + body.get("email"));
        String email = body.get("email");
        String password = body.get("password");
        String selectedRole = body.get("selectedRole");

        Map<String, Object> result = authService.login(email, password, selectedRole);

        if ((boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String password = (String) body.get("password");
        String role = (String) body.get("role");

        @SuppressWarnings("unchecked")
        List<String> skills = body.containsKey("skills") ? (List<String>) body.get("skills") : List.of();

        Map<String, Object> result = authService.register(name, email, password, role, skills);

        if ((boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(400).body(result);
        }
    }
}
