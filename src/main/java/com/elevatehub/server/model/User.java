package com.elevatehub.server.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    /** "student", "mentor", or "admin" */
    private String role;

    /** Mentor only: "approved", "pending", or "rejected" */
    private String status;

    /** Mentor only: list of skill tags — stored as comma-separated in a TEXT column */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills;

    /** Mentor only: profile image URL */
    @Column(length = 500)
    private String profileImage;

    /** Mentor only: availability */
    private String weekdayStart;
    private String weekdayEnd;
    private String weekendStart;
    private String weekendEnd;

    /** Mentor only: tracks last time "Go Offline" was used (14-day cooldown) */
    private Instant lastOfflineDate;

    private Instant createdAt;

    // ─── Constructors ──────────────────────────────────────────

    public User() {
        this.createdAt = Instant.now();
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = Instant.now();
    }

    // ─── Getters & Setters ─────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getWeekdayStart() { return weekdayStart; }
    public void setWeekdayStart(String weekdayStart) { this.weekdayStart = weekdayStart; }

    public String getWeekdayEnd() { return weekdayEnd; }
    public void setWeekdayEnd(String weekdayEnd) { this.weekdayEnd = weekdayEnd; }

    public String getWeekendStart() { return weekendStart; }
    public void setWeekendStart(String weekendStart) { this.weekendStart = weekendStart; }

    public String getWeekendEnd() { return weekendEnd; }
    public void setWeekendEnd(String weekendEnd) { this.weekendEnd = weekendEnd; }

    public Instant getLastOfflineDate() { return lastOfflineDate; }
    public void setLastOfflineDate(Instant lastOfflineDate) { this.lastOfflineDate = lastOfflineDate; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
