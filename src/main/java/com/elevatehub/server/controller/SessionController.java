package com.elevatehub.server.controller;

import com.elevatehub.server.model.Session;
import com.elevatehub.server.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /** Get all sessions for a student */
    @GetMapping("/student/{userId}")
    public ResponseEntity<List<Session>> getStudentSessions(@PathVariable String userId) {
        return ResponseEntity.ok(sessionService.getStudentSessions(userId));
    }

    /** Get all sessions for a mentor */
    @GetMapping("/mentor/{userId}")
    public ResponseEntity<List<Session>> getMentorSessions(@PathVariable String userId) {
        return ResponseEntity.ok(sessionService.getMentorSessions(userId));
    }

    /** Book a new session */
    @PostMapping
    public ResponseEntity<Map<String, Object>> bookSession(@RequestBody Map<String, String> body) {
        Map<String, Object> result = sessionService.bookSession(
                body.get("studentId"),
                body.get("mentorId"),
                body.get("date"),
                body.get("time"),
                body.get("topic")
        );
        return ResponseEntity.ok(result);
    }

    /** Accept or decline a session request */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Map<String, Object> result = sessionService.updateSessionStatus(id, body.get("action"));
        return ResponseEntity.ok(result);
    }

    /** Add meeting link to a session */
    @PutMapping("/{id}/link")
    public ResponseEntity<Map<String, Object>> addMeetingLink(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Map<String, Object> result = sessionService.addMeetingLink(id, body.get("meetingLink"));
        return ResponseEntity.ok(result);
    }
}
