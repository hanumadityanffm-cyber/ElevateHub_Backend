package com.elevatehub.server.service;

import com.elevatehub.server.model.Session;
import com.elevatehub.server.model.User;
import com.elevatehub.server.repository.SessionRepository;
import com.elevatehub.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all sessions for a student.
     */
    public List<Session> getStudentSessions(String studentId) {
        return sessionRepository.findByStudentId(studentId);
    }

    /**
     * Get all sessions for a mentor.
     */
    public List<Session> getMentorSessions(String mentorId) {
        return sessionRepository.findByMentorId(mentorId);
    }

    /**
     * Book a new session (student → mentor).
     */
    public Map<String, Object> bookSession(String studentId, String mentorId, String date, String time, String topic) {
        Map<String, Object> result = new HashMap<>();

        Optional<User> optStudent = userRepository.findById(studentId);
        Optional<User> optMentor = userRepository.findById(mentorId);

        if (optStudent.isEmpty() || optMentor.isEmpty()) {
            result.put("success", false);
            result.put("message", "Student or mentor not found.");
            return result;
        }

        Session session = new Session();
        session.setStudentId(studentId);
        session.setStudentName(optStudent.get().getName());
        session.setMentorId(mentorId);
        session.setMentorName(optMentor.get().getName());
        session.setDate(date);
        session.setTime(time);
        session.setTopic(topic != null ? topic : "General Mentoring");
        session.setStatus("Pending Approval");
        session.setMeetingLink(null);

        sessionRepository.save(session);

        result.put("success", true);
        result.put("session", session);
        return result;
    }

    /**
     * Accept or decline a session request (mentor action).
     */
    public Map<String, Object> updateSessionStatus(String sessionId, String action) {
        Map<String, Object> result = new HashMap<>();
        Optional<Session> optSession = sessionRepository.findById(sessionId);

        if (optSession.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found.");
            return result;
        }

        Session session = optSession.get();
        session.setStatus("accept".equals(action) ? "Approved" : "Declined");
        sessionRepository.save(session);

        result.put("success", true);
        result.put("session", session);
        return result;
    }

    /**
     * Add meeting link to a session (mentor action).
     */
    public Map<String, Object> addMeetingLink(String sessionId, String meetingLink) {
        Map<String, Object> result = new HashMap<>();
        Optional<Session> optSession = sessionRepository.findById(sessionId);

        if (optSession.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found.");
            return result;
        }

        Session session = optSession.get();
        session.setMeetingLink(meetingLink);
        sessionRepository.save(session);

        result.put("success", true);
        result.put("session", session);
        return result;
    }
}
