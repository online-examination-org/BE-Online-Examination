package com.team2.online_examination.services;

import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.dtos.requests.QuestionCreateRequest;
import com.team2.online_examination.dtos.requests.QuestionUpdateRequest;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.exceptions.QuestionExistedException;
import com.team2.online_examination.mappers.ExamMapper;
import com.team2.online_examination.mappers.QuestionMapper;
import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.repositories.ExamRepository;
import com.team2.online_examination.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ExamRepository examRepository) {
        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
    }

    public List<Question> findAllByExamId(long examId) {
        if (examId == 0) return questionRepository.findAll();
        return questionRepository.findAllByExam_ExamId(examId);
    }

    public Optional<Question> findByQuestionId(long questionId) {
        return questionRepository.findByQuestionId(questionId);
    }

    public Iterable<Question> saveAll(List<QuestionCreateRequest> questionCreateRequests) {
        System.out.print(questionCreateRequests);
        List<Question> addedQuestion = new ArrayList<>();
        for (QuestionCreateRequest questionCreateRequest : questionCreateRequests) {
            Question question = QuestionMapper.INSTANCE.toQuestion(questionCreateRequest);
            Exam exam = examRepository.findById(questionCreateRequest.getExam_id()).orElseThrow(
                    () -> new NotFoundException("Exam not found with id: " + questionCreateRequest.getExam_id())
            );
            question.setExam(exam);
            addedQuestion.add(question);
            questionRepository.save(question);
        }
        return addedQuestion;
    }

    public void updateQuestion(QuestionUpdateRequest questionUpdateRequest, Long id, Long exam_id) throws NotFoundException {
        Question updateQuestion = QuestionMapper.INSTANCE.toQuestion(questionUpdateRequest);
        Question question = questionRepository.findByQuestionId(id).orElseThrow(
                () -> new NotFoundException("Question not found with id: " + id)
        );
        Exam exam = examRepository.findById(exam_id).orElseThrow(
                () -> new NotFoundException("Exam not found with id: " + exam_id)
        );
        if(!question.getExam().getExamId().equals(exam_id)){
            throw new NotFoundException("Exam not found");
        }
        question.setQuestionText(updateQuestion.getQuestionText()!=null? updateQuestion.getQuestionText() : question.getQuestionText());
        question.setQuestionType(updateQuestion.getQuestionType()!=null? updateQuestion.getQuestionType() : question.getQuestionType());
        question.setAnswer(updateQuestion.getAnswer()!=null? updateQuestion.getAnswer() : question.getAnswer());
        question.setChoices(updateQuestion.getChoices());

        questionRepository.save(question);
    }
    public void deleteQuestion(long id, long exam_id) {
        Question question = questionRepository.findByQuestionId(id).orElseThrow(
                () -> new NotFoundException("Question not found with id: " + id)
        );
        Exam exam = examRepository.findById(exam_id).orElseThrow(
                () -> new NotFoundException("Exam not found with id: " + exam_id)
        );
        if(!question.getExam().getExamId().equals(exam_id)){
            throw new NotFoundException("Exam not found");
        }
        questionRepository.delete(question);
    }
}
