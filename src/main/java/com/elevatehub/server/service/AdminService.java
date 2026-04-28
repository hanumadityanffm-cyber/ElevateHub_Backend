package com.elevatehub.server.service;

import com.elevatehub.server.model.User;
import com.elevatehub.server.repository.SessionRepository;
import com.elevatehub.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public AdminService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Get platform statistics for the admin dashboard.
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMentees", userRepository.findByRole("student").size());
        stats.put("totalMentors", userRepository.findByRoleAndStatus("mentor", "approved").size());
        stats.put("activeSessions", sessionRepository.countByStatus("Approved"));
        return stats;
    }

    /**
     * Get all pending mentor applications.
     */
    public List<Map<String, Object>> getPendingApplications() {
        List<User> pending = userRepository.findByRoleAndStatus("mentor", "pending");
        List<Map<String, Object>> result = new ArrayList<>();

        for (User u : pending) {
            Map<String, Object> app = new HashMap<>();
            app.put("id", u.getId());
            app.put("name", u.getName());
            // Generate initials from name
            String[] parts = u.getName().split(" ");
            StringBuilder initials = new StringBuilder();
            for (String p : parts) {
                if (!p.isEmpty()) initials.append(p.charAt(0));
            }
            app.put("initials", initials.toString().toUpperCase());
            app.put("skills", u.getSkills() != null ? u.getSkills() : List.of());
            app.put("date", u.getCreatedAt() != null ? u.getCreatedAt().toString().substring(0, 10) : "Unknown");
            result.add(app);
        }
        return result;
    }

    /**
     * Approve or reject a mentor application.
     */
    public Map<String, Object> handleApplication(String userId, String action) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isEmpty()) {
            result.put("success", false);
            result.put("message", "Application not found.");
            return result;
        }

        User user = optUser.get();
        user.setStatus("approve".equals(action) ? "approved" : "rejected");
        userRepository.save(user);

        result.put("success", true);
        result.put("name", user.getName());
        result.put("action", action);
        return result;
    }

    /** Get all users by role (excluding current admin for safety) */
    public List<Map<String, Object>> getUsersByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        List<Map<String, Object>> result = new ArrayList<>();

        for (User u : users) {
           // For students, all are "approved" by default. For mentors, only show approved ones in "Manage".
           if ("mentor".equals(role) && !"approved".equals(u.getStatus())) continue;

           Map<String, Object> map = new HashMap<>();
           map.put("id", u.getId());
           map.put("name", u.getName());
           map.put("email", u.getEmail());
           // Initials
           String[] p = u.getName().split(" ");
           String ini = (p.length > 0 ? p[0].substring(0,1) : "") + (p.length > 1 ? p[1].substring(0,1) : "");
           map.put("initials", ini.toUpperCase());
           result.add(map);
        }
        return result;
    }

    /** Create a user manually from Admin Control Center */
    public Map<String, Object> createUser(String name, String email, String password, String role, List<String> skills) {
        Map<String, Object> res = new HashMap<>();
        if (userRepository.existsByEmail(email.toLowerCase().trim())) {
            res.put("success", false);
            res.put("message", "Email already exists.");
            return res;
        }

        User user = new User(name, email.toLowerCase().trim(), password, role);
        user.setStatus("approved");
        user.setSkills(skills);
        
        if ("mentor".equals(role)) {
            user.setProfileImage("https://ui-avatars.com/api/?name=" + name.replace(" ", "+") + "&background=random");
            user.setWeekdayStart("09:00 AM");
            user.setWeekdayEnd("06:00 PM");
            user.setWeekendStart("Off / Unavailable");
            user.setWeekendEnd("Off / Unavailable");
        } else {
            user.setProfileImage("https://ui-avatars.com/api/?name=" + name.replace(" ", "+") + "&background=010101&color=fff");
        }
        
        userRepository.save(user);
        res.put("success", true);
        return res;
    }

    /** Delete a user and all their sessions */
    public Map<String, Object> deleteUser(String id) {
        Map<String, Object> res = new HashMap<>();
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            // Delete sessions where they were mentor OR student
            sessionRepository.deleteAll(sessionRepository.findByMentorId(id));
            sessionRepository.deleteAll(sessionRepository.findByStudentId(id));
            userRepository.delete(user);
            res.put("success", true);
        } else {
            res.put("success", false);
            res.put("message", "User not found.");
        }
        return res;
    }
}
