package com.team2.online_examination.services;

import com.team2.online_examination.models.ExamResult;
import com.team2.online_examination.models.ExamResultDetail;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.repositories.ExamResultDetailRepository;
import com.team2.online_examination.repositories.ExamResultRepository;
import com.team2.online_examination.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExamResultDetailService {

    private final ExamResultDetailRepository examResultDetailRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;

    @Autowired
    public ExamResultDetailService(
            ExamResultDetailRepository examResultDetailRepository,
            QuestionRepository questionRepository,
            ExamResultRepository examResultRepository
    ) {
        this.examResultDetailRepository = examResultDetailRepository;
        this.questionRepository = questionRepository;
        this.examResultRepository = examResultRepository;
    }

    public void updateResponse(Long examResultId, Long questionId, String response) {
        ExamResultDetail examResultDetail = getExamResultDetail(examResultId, questionId);
        if (examResultDetail == null) {
            createNewExamResultDetail(examResultId, questionId, response);
        } else {
            examResultDetail.setResponse(response);
            examResultDetailRepository.save(examResultDetail);
        }
    }

    private ExamResultDetail getExamResultDetail(Long examResultId, Long questionId) {
        return examResultDetailRepository
                .findByExamResult_ExamResultIdAndQuestion_QuestionId(examResultId, questionId)
                .orElse(null);
    }

    private void createNewExamResultDetail(Long examResultId, Long questionId, String response) {
        Question question = getQuestion(questionId);
        ExamResult examResult = getExamResult(examResultId);

        ExamResultDetail newDetail = new ExamResultDetail();
        newDetail.setExamResult(examResult);
        newDetail.setQuestion(question);
        newDetail.setResponse(response);
        newDetail.setIsCorrect(false); // default value
        examResultDetailRepository.save(newDetail);
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with ID " + questionId + " not found"));
    }

    private ExamResult getExamResult(Long examResultId) {
        return examResultRepository.findById(examResultId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ExamResult with ID " + examResultId + " not found"));
    }

    public boolean submit(Long examResultDetailId) {
        ExamResultDetail examResultDetail = getExamResultDetailById(examResultDetailId);
        String response = examResultDetail.getResponse();
        Question question = examResultDetail.getQuestion();

        boolean isCorrect = response.equals(question.getAnswer());
        examResultDetail.setIsCorrect(isCorrect);
        examResultDetailRepository.save(examResultDetail);

        return isCorrect;
    }

    private ExamResultDetail getExamResultDetailById(Long examResultDetailId) {
        return examResultDetailRepository.findByExamResultDetailId(examResultDetailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ExamResultDetail with ID " + examResultDetailId + " not found"));
    }

    public List<ExamResultDetail> getExamResultDetailsByExamResultId(Long examResultId) {
        return examResultDetailRepository.findByExamResult_ExamResultId(examResultId);
    }
}
