package com.team2.online_examination.repositories;

import com.team2.online_examination.models.Question;
import com.team2.online_examination.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT s FROM question s WHERE s.exam.exam_id = :exam_id")
    List<Question> findAllByExamId(Long exam_id);
    @Query(value = "SELECT s FROM question s WHERE s.question_id = :questionId")
    List<Question> findAllByQuestionId(long questionId);
}
