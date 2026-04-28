package com.elevatehub.server.repository;

import com.elevatehub.server.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    List<Session> findByStudentId(String studentId);

    List<Session> findByMentorId(String mentorId);

    List<Session> findByMentorIdAndStatus(String mentorId, String status);

    long countByStatus(String status);
}
