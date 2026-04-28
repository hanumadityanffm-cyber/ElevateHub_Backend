package com.elevatehub.server.config;

import com.elevatehub.server.model.User;
import com.elevatehub.server.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds exactly 3 custom Aditya accounts for the final version.
 * Drops all users first to ensure clean state.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n── CLEANING PLATFORM & SEEDING FINAL ACCOUNTS ──");

        // Clear everything first
        userRepository.deleteAll();

        // 1. Aditya - STUDENT
        seedUser("Aditya Varma", "aditya.student@elevate.in", "123456", "student", "approved", null, 
                "https://ui-avatars.com/api/?name=Aditya+Student&background=010101&color=fff");

        // 2. Aditya - MENTOR
        seedMentor("Aditya Varma", "aditya.mentor@elevate.in", "123456", "approved",
                List.of("Full Stack Development", "Spring Boot", "React", "MongoDB", "UI/UX Design"),
                "https://ui-avatars.com/api/?name=Aditya+Mentor&background=C967E8&color=fff");

        // 3. Aditya - ADMIN
        seedUser("Aditya Varma", "aditya.admin@elevate.in", "123456", "admin", null, null, 
                "https://ui-avatars.com/api/?name=Aditya+Admin&background=3b82f6&color=fff");

        System.out.println("── Platform is ready with 3 Aditya Accounts! ──\n");
    }

    private void seedUser(String name, String email, String password, String role, String status, List<String> skills, String image) {
        User user = new User(name, email, password, role);
        user.setStatus(status);
        user.setSkills(skills);
        user.setProfileImage(image);
        userRepository.save(user);
        System.out.println("  ✓ Created " + role + ": " + email);
    }

    private void seedMentor(String name, String email, String password, String status, List<String> skills, String image) {
        User mentor = new User(name, email, password, "mentor");
        mentor.setStatus(status);
        mentor.setSkills(skills);
        mentor.setProfileImage(image);
        mentor.setWeekdayStart("09:00 AM");
        mentor.setWeekdayEnd("06:00 PM");
        mentor.setWeekendStart("Off / Unavailable");
        mentor.setWeekendEnd("Off / Unavailable");
        userRepository.save(mentor);
        System.out.println("  ✓ Created mentor (approved): " + email);
    }
}
