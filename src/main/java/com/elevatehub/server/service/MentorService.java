package com.elevatehub.server.service;

import com.elevatehub.server.model.Session;
import com.elevatehub.server.model.User;
import com.elevatehub.server.repository.SessionRepository;
import com.elevatehub.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class MentorService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public MentorService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Get all approved mentors (for student browse page).
     */
    public List<Map<String, Object>> getApprovedMentors() {
        List<User> mentors = userRepository.findByRoleAndStatus("mentor", "approved");
        List<Map<String, Object>> result = new ArrayList<>();

        for (User m : mentors) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("name", m.getName());
            map.put("role", "Senior Mentor"); // Display role for the card
            map.put("tags", m.getSkills() != null ? m.getSkills() : List.of());
            map.put("image", m.getProfileImage());
            result.add(map);
        }
        return result;
    }

    /**
     * Update mentor availability settings.
     */
    public Map<String, Object> updateAvailability(String mentorId, String weekdayStart, String weekdayEnd, String weekendStart, String weekendEnd) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> optUser = userRepository.findById(mentorId);

        if (optUser.isEmpty()) {
            result.put("success", false);
            result.put("message", "Mentor not found.");
            return result;
        }

        User mentor = optUser.get();
        mentor.setWeekdayStart(weekdayStart);
        mentor.setWeekdayEnd(weekdayEnd);
        mentor.setWeekendStart(weekendStart);
        mentor.setWeekendEnd(weekendEnd);
        userRepository.save(mentor);

        result.put("success", true);
        return result;
    }

    /**
     * Emergency "Go Offline" — shifts all active sessions for this mentor forward by 1 day.
     * Has a 14-day cooldown.
     */
    public Map<String, Object> goOffline(String mentorId) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> optUser = userRepository.findById(mentorId);

        if (optUser.isEmpty()) {
            result.put("success", false);
            result.put("message", "Mentor not found.");
            return result;
        }

        User mentor = optUser.get();

        // Check 14-day cooldown
        if (mentor.getLastOfflineDate() != null) {
            long daysSince = Duration.between(mentor.getLastOfflineDate(), Instant.now()).toDays();
            if (daysSince < 14) {
                result.put("success", false);
                result.put("locked", true);
                result.put("daysRemaining", 14 - daysSince);
                return result;
            }
        }

        // Shift all active sessions forward by 1 day
        List<Session> sessions = sessionRepository.findByMentorId(mentorId);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);

        for (Session s : sessions) {
            try {
                Date d = sdf.parse(s.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.DATE, 1);
                s.setDate(sdf.format(cal.getTime()));
                sessionRepository.save(s);
            } catch (Exception e) {
                // Skip malformed dates
            }
        }

        mentor.setLastOfflineDate(Instant.now());
        userRepository.save(mentor);

        result.put("success", true);
        return result;
    }
}
