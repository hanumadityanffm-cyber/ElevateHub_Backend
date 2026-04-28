package com.elevatehub.server.service;

import com.elevatehub.server.model.User;
import com.elevatehub.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticate user and enforce role-based access.
     * Returns a result map with either user data or error info.
     */
    public Map<String, Object> login(String email, String password, String selectedRole) {
        System.out.println("\n--- LOGIN ATTEMPT ---");
        System.out.println("Email: [" + email + "]");
        System.out.println("Role selected in UI: [" + selectedRole + "]");

        Map<String, Object> result = new HashMap<>();
        Optional<User> optUser = userRepository.findByEmail(email.toLowerCase().trim());

        if (optUser.isEmpty()) {
            System.out.println("Result: FAILED - User not found in MySQL");
            result.put("success", false);
            result.put("error", "INVALID_CREDENTIALS");
            result.put("message", "No account found with those credentials.");
            return result;
        }

        User user = optUser.get();
        System.out.println("Found user in DB with role: [" + user.getRole() + "]");

        // Check password
        if (!user.getPassword().equals(password)) {
            result.put("success", false);
            result.put("error", "INVALID_CREDENTIALS");
            result.put("message", "Incorrect password.");
            return result;
        }

        // Case 2: Rejected mentor
        if ("mentor".equals(user.getRole()) && "rejected".equals(user.getStatus())) {
            result.put("success", false);
            result.put("error", "MENTOR_REJECTED");
            result.put("message", "Your mentorship application was not accepted.");
            return result;
        }

        // Case 3: Pending mentor (not yet approved by admin)
        if ("mentor".equals(user.getRole()) && "pending".equals(user.getStatus())) {
            result.put("success", false);
            result.put("error", "MENTOR_PENDING");
            result.put("message", "Your application is still under review.");
            return result;
        }

        // Case 4: Role mismatch
        if (!user.getRole().equals(selectedRole)) {
            result.put("success", false);
            result.put("error", "ROLE_MISMATCH");
            result.put("message", "Credentials belong to a " + user.getRole() + " account.");
            result.put("actualRole", user.getRole());
            return result;
        }

        // Case 5: Success
        result.put("success", true);
        result.put("id", user.getId());
        result.put("name", user.getName());
        result.put("email", user.getEmail());
        result.put("role", user.getRole());
        result.put("status", user.getStatus());
        return result;
    }

    /**
     * Register a new student or submit a mentor application.
     */
    public Map<String, Object> register(String name, String email, String password, String role, java.util.List<String> skills) {
        Map<String, Object> result = new HashMap<>();

        if (userRepository.existsByEmail(email.toLowerCase().trim())) {
            result.put("success", false);
            result.put("error", "EMAIL_EXISTS");
            result.put("message", "An account with this email already exists.");
            return result;
        }

        User user = new User(name, email.toLowerCase().trim(), password, role);

        if ("mentor".equals(role)) {
            user.setStatus("pending");
            user.setSkills(skills);
            user.setProfileImage("https://ui-avatars.com/api/?name=" + name.replace(" ", "+") + "&background=random");
            // Default availability
            user.setWeekdayStart("09:00 AM");
            user.setWeekdayEnd("06:00 PM");
            user.setWeekendStart("Off / Unavailable");
            user.setWeekendEnd("Off / Unavailable");
        } else {
            user.setStatus("approved");
        }

        userRepository.save(user);

        result.put("success", true);
        result.put("id", user.getId());
        result.put("name", user.getName());
        result.put("email", user.getEmail());
        result.put("role", user.getRole());
        result.put("status", user.getStatus());
        return result;
    }
}
