package com.team2.online_examination.repositories;

import com.team2.online_examination.models.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    @Query("SELECT er FROM ExamResult er WHERE er.exam.examId = :examId AND (er.exam.endTime < :currentTime OR er.finishedAt IS NOT NULL)")
    List<ExamResult> findAllByExam_ExamId( Long examId,  LocalDateTime currentTime);
    ExamResult findByExamResultId(Long examResultId);

}
