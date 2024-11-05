package com.team2.online_examination.repositories;

import com.team2.online_examination.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByTeacher_Id(Long teacherId);
    Optional<Exam> findByPasscode(String passcode);
}
