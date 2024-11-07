package com.team2.online_examination.repositories;

import com.team2.online_examination.models.ExamResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultDetailRepository extends JpaRepository<ExamResultDetail, Long> {
    Optional<ExamResultDetail> findByExamResultDetailId(Long examResultDetailId);
    Optional<ExamResultDetail> findByExamResult_ExamResultIdAndQuestion_QuestionId(Long examResultId, Long questionId);
    List<ExamResultDetail> findByExamResult_ExamResultId(Long examResultId);
}
