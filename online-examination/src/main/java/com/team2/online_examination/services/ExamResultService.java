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
import com.team2.online_examination.models.ExamResultDetail;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.repositories.ExamRepository;
import com.team2.online_examination.repositories.ExamResultRepository;
import com.team2.online_examination.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultDetailService examResultDetailService;

    @Autowired
    public ExamResultService(
            ExamResultRepository examResultRepository,
            ExamRepository examRepository,
            QuestionRepository questionRepository,
            ExamResultDetailService examResultDetailService
    ) {
        this.examResultRepository = examResultRepository;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.examResultDetailService = examResultDetailService;

    }

    public ExamResultCreateResponse addExamResult(ExamResultCreateRequest examResultCreateRequest) throws NotFoundException {
        ExamResult examResult = ExamResultMapper.INSTANCE.toExamResult(examResultCreateRequest);
        Exam exam = examRepository.findById(examResultCreateRequest.getExamId()).orElseThrow(
                ()-> new NotFoundException("Exam not found with id: " + examResultCreateRequest.getExamId())
        );
        if(!exam.getIsActive()){
            throw new NotFoundException("Exam not found with id: " + examResultCreateRequest.getExamId());
        }
        if(exam.getEndTime().isBefore(LocalDateTime.now())){
            throw new NotFoundException("Exam has already finished");
        }
        examResult.setExam(exam);
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
        if (examResult.getStartedAt() != null) {
            System.out.println(examResult.getStartedAt());
            if (examResult.getStartedAt().isAfter(LocalDateTime.now())) {
                throw new BadRequestException("Exam has not started yet");
            }
        }
        if(examResult.getExam().getEndTime()!=null&&examResult.getExam().getEndTime().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Exam has already finished");
        }
        if(!examResult.getExam().getIsActive()){
            throw new BadRequestException("Exam has not started yet");
        }
        if(examResult.getFinishedAt()!=null){
            throw new BadRequestException("Exam has already finished");
        }
        if(examResult.getCreatedAt()==null){
            examResult.setStartedAt(examResultUpdateRequest.getStartedAt());
            examResultRepository.save(examResult);
        }
        List<Question> questions = questionRepository.findAllByExam_ExamId(examResult.getExam().getExamId());
        return QuestionMapper.INSTANCE.toExamResultUpdateResponseList(questions);
    }
    public List<ExamResult> getAllExamResultByExamId(Long examId) {
        return examResultRepository.findAllByExam_ExamId(examId);
    }

    public void submit(Long examResultId, LocalDateTime finishAt) throws NotFoundException, BadRequestException {
        ExamResult examResult = examResultRepository.findById(examResultId)
                .orElseThrow(() -> new NotFoundException("Exam result not found with id: " + examResultId));

        if (examResult.getFinishedAt() != null) {
            throw new BadRequestException("Exam has already been submitted");
        }

        // Validate that each question in the exam has been submitted
        Long examId = examResult.getExam().getExamId();
        List<Question> questions = questionRepository.findAllByExam_ExamId(examId);
        if (questions.isEmpty()) {
            throw new BadRequestException("No questions found for this exam result");
        }

        // Fetch all examResultDetail
        List<ExamResultDetail> examResultDetails = examResultDetailService.getExamResultDetailsByExamResultId(examResultId);

        // Calculate score based on correct answers
        float totalScore = 0;
        for (ExamResultDetail examResultDetail : examResultDetails) {
            Long examResultDetailId = examResultDetail.getExamResultDetailId();
            boolean isCorrect = examResultDetailService.submit(examResultDetailId);
            if (isCorrect) {
                totalScore += 1;
            }
        }

        // Update score and finishedAt
        float percentageScore = (totalScore / questions.size()) * 100;
        examResult.setScore(percentageScore);
        examResult.setFinishedAt(finishAt != null && finishAt.isBefore(LocalDateTime.now()) ? finishAt : LocalDateTime.now());

        // Save submission
        examResultRepository.save(examResult);
    }
}
