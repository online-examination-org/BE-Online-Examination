package com.team2.online_examination.services;

import com.team2.online_examination.exceptions.EmailExistedException;
import com.team2.online_examination.exceptions.QuestionExistedException;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.repositories.QuestionRepository;
import com.team2.online_examination.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class QuestionService {
    private final QuestionRepository questionRepository;
    private final JwtUtil jwtUtil;
    @Autowired
    public QuestionService(
            QuestionRepository teacherRepository,
            QuestionRepository questionRepository, JwtUtil jwtUtil
    ) {
        this.questionRepository = questionRepository;
        this.jwtUtil = jwtUtil;
    }
    public List<Question> findAllByExam_id(long exam_id) {
        return questionRepository.findAllByExamId(exam_id);
    }
    public List<Question> findAllByQuestionId(long questionId) {
        return questionRepository.findAllByQuestionId(questionId);
    }
    public Iterable<Question> saveAll(Iterable<Question> questions) {
        for (Question question : questions) {
            List<Question> findQuestion = questionRepository.findAllByQuestionId(question.getQuestion_id());
            if (findQuestion.isEmpty()) {
                throw new QuestionExistedException("Question is existed!");
            }
        }
        return questionRepository.saveAll(questions);

    };
}
