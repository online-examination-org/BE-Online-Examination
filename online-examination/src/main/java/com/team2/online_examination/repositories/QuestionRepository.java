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
    List<Question> findAllByExam_ExamId(Long exam_id);

    Optional<Question> findByQuestionId(Long questionId);
<<<<<<< Updated upstream

=======
    void deleteAllByExam_ExamId(Long examId);
>>>>>>> Stashed changes
}
