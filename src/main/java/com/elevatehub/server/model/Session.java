package com.elevatehub.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;
    private String studentName;

    private String mentorId;
    private String mentorName;

    /** e.g. "Apr 14, 2026" */
    private String date;

    /** e.g. "10:00 AM (IST)" */
    private String time;

    /** "Pending Approval", "Approved", or "Declined" */
    private String status;

    /** Zoom / Google Meet URL — null until mentor adds it */
    @Column(length = 500)
    private String meetingLink;

    /** Topic of the session request */
    private String topic;

    private Instant createdAt;

    // ─── Constructors ──────────────────────────────────────────

    public Session() {
        this.createdAt = Instant.now();
    }

    // ─── Getters & Setters ─────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMentorId() {
        return mentorId;
    }

    public void setMentorId(String mentorId) {
        this.mentorId = mentorId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
