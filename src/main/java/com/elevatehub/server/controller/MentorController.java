package com.elevatehub.server.controller;

import com.elevatehub.server.service.MentorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentors")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    /** List all approved mentors for student browse page */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getApprovedMentors() {
        return ResponseEntity.ok(mentorService.getApprovedMentors());
    }

    /** Update mentor availability (weekday/weekend timings) */
    @PutMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> updateAvailability(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Map<String, Object> result = mentorService.updateAvailability(
                id,
                body.get("weekdayStart"),
                body.get("weekdayEnd"),
                body.get("weekendStart"),
                body.get("weekendEnd")
        );
        return ResponseEntity.ok(result);
    }

    /** Emergency Go Offline — shift all sessions +1 day with 14-day cooldown */
    @PutMapping("/{id}/offline")
    public ResponseEntity<Map<String, Object>> goOffline(@PathVariable String id) {
        Map<String, Object> result = mentorService.goOffline(id);
        return ResponseEntity.ok(result);
    }
}
