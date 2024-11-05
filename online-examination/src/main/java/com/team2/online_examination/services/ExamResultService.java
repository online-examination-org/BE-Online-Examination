package com.team2.online_examination.services;

import com.team2.online_examination.dtos.requests.ExamResultCreateRequest;
import com.team2.online_examination.dtos.requests.ExamResultUpdateRequest;
import com.team2.online_examination.dtos.responses.ExamCreateResponse;
import com.team2.online_examination.dtos.responses.ExamResultCreateResponse;
import com.team2.online_examination.dtos.responses.ExamResultUpdateResponse;
import com.team2.online_examination.exceptions.BadRequestException;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.mappers.ExamMapper;
import com.team2.online_examination.mappers.ExamResultMapper;
import com.team2.online_examination.mappers.QuestionMapper;
import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.ExamResult;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.repositories.ExamRepository;
import com.team2.online_examination.repositories.ExamResultRepository;
import com.team2.online_examination.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
public class ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public ExamResultService(ExamResultRepository examResultRepository, ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examResultRepository = examResultRepository;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    public ExamResultCreateResponse addExamResult(ExamResultCreateRequest examResultCreateRequest) throws NotFoundException {
        ExamResult examResult = ExamResultMapper.INSTANCE.toExamResult(examResultCreateRequest);
        Exam exam = examRepository.findById(examResultCreateRequest.getExamId()).orElseThrow(
                ()-> new NotFoundException("Exam not found with id: " + examResultCreateRequest.getExamId())
        );
        examResult.setExam(exam);
        examResult.setScore(0f);
        //Có mssv ko?
        examResult.setStartedAt(LocalDateTime.now()); // Cần hỏi lại sửa lại nullable = true hay để như vầy
        examResult.setFinishedAt(LocalDateTime.now()); // Cần hỏi lại sửa lại nullable = true hay để như vầy
        examResultRepository.save(examResult);

        ExamResultCreateResponse examCreateResponse = ExamResultMapper.INSTANCE.toExamResultCreateResponse(examResult);
        examCreateResponse.setExamGetResponse(ExamMapper.INSTANCE.toExamGetResponse(exam));
        return examCreateResponse;
    }
    public List<ExamResultUpdateResponse> accessExam(ExamResultUpdateRequest examResultUpdateRequest,Long id) throws NotFoundException, BadRequestException {
        ExamResult examResult = examResultRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Exam result not found with id: " + id)
        );
        if(!Objects.equals(examResult.getExam().getExamId(), examResultUpdateRequest.getExamId())){
            throw new NotFoundException("Exam result not found with id: " + id);
        }
        if(examResult.getStartedAt().isAfter(LocalDateTime.now())){
            throw new BadRequestException("Exam has not started yet");
        }
        if(!examResult.getExam().getIsActive()){
            throw new BadRequestException("Exam has not started yet");
        }
        if(examResult.getFinishedAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Exam has finished");
        }
        LocalDateTime createdAt = examResult.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (examResult.getUpdatedAt().equals(createdAt)) {
            examResult.setStartedAt(examResultUpdateRequest.getStartedAt());
            examResultRepository.save(examResult);
        }
        List<Question> questions = questionRepository.findAllByExam_ExamId(examResult.getExam().getExamId());
        return QuestionMapper.INSTANCE.toExamResultUpdateResponseList(questions);
    }

}
