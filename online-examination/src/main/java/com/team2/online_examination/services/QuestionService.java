package com.team2.online_examination.services;

import com.team2.online_examination.exceptions.QuestionExistedException;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findAllByExamId(long examId) {
        return questionRepository.findAllByExam_ExamId(examId);
    }

    public Optional<Question> findByQuestionId(long questionId) {
        return questionRepository.findByQuestionId(questionId);
    }

    public Iterable<Question> saveAll(Iterable<Question> questions) {
        for (Question question : questions) {
            Optional<Question> findQuestion = questionRepository.findByQuestionId(question.getQuestionId());
            if (findQuestion.isPresent()) {
                throw new QuestionExistedException("Question already exists!");
            }
        }
        return questionRepository.saveAll(questions);
    }
}
