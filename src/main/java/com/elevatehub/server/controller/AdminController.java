package com.elevatehub.server.controller;

import com.elevatehub.server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Get platform statistics (total students, mentors, active sessions) */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    /** Get all pending mentor applications */
    @GetMapping("/applications")
    public ResponseEntity<List<Map<String, Object>>> getPendingApplications() {
        return ResponseEntity.ok(adminService.getPendingApplications());
    }

    @PutMapping("/applications/{id}")
    public ResponseEntity<Map<String, Object>> handleApplication(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Map<String, Object> result = adminService.handleApplication(id, body.get("action"));
        return ResponseEntity.ok(result);
    }

    /** User Management: All Students (Admins only) */
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getUsers(@RequestParam String role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }

    /** Create a manual student/mentor */
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String password = (String) body.get("password");
        String role = (String) body.get("role");
        
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) body.getOrDefault("skills", List.of());
        
        return ResponseEntity.ok(adminService.createUser(name, email, password, role, skills));
    }

    /** Delete student/mentor and sessions */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }
}
