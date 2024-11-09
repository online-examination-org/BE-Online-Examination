package com.team2.online_examination.repositories;

import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findAllByExam_ExamId(Long examId);
    ExamResult findByExamResultId(Long examResultId);
}
