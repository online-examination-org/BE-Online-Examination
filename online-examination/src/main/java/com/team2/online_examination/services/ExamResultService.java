package com.team2.online_examination.services;

import com.team2.online_examination.dtos.JwtPayload;
import com.team2.online_examination.dtos.StudentJwtPayload;
import com.team2.online_examination.dtos.requests.ExamResultCreateRequest;
import com.team2.online_examination.dtos.requests.ExamResultUpdateRequest;
import com.team2.online_examination.dtos.responses.ExamResultCreateResponse;
import com.team2.online_examination.dtos.responses.ExamResultUpdateResponse;
import com.team2.online_examination.exceptions.BadRequestException;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.mappers.ExamMapper;
import com.team2.online_examination.mappers.ExamResultMapper;
import com.team2.online_examination.mappers.QuestionMapper;
import com.team2.online_examination.mappers.StudentMapper;
import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.ExamResult;
import com.team2.online_examination.models.ExamResultDetail;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.repositories.ExamRepository;
import com.team2.online_examination.repositories.ExamResultRepository;
import com.team2.online_examination.repositories.QuestionRepository;
import com.team2.online_examination.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Service
public class ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultDetailService examResultDetailService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ExamResultService(
            ExamResultRepository examResultRepository,
            ExamRepository examRepository,
            QuestionRepository questionRepository,
            ExamResultDetailService examResultDetailService, JwtUtil jwtUtil
    ) {
        this.examResultRepository = examResultRepository;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.examResultDetailService = examResultDetailService;
        this.jwtUtil = jwtUtil;
    }

    public ExamResultCreateResponse addExamResult(ExamResultCreateRequest examResultCreateRequest) throws NotFoundException {
        ExamResult examResult = ExamResultMapper.INSTANCE.toExamResult(examResultCreateRequest);
        Exam exam = examRepository.findByPasscode(examResultCreateRequest.getPasscode()).orElseThrow(
                ()-> new NotFoundException("Exam not found with id: " + examResultCreateRequest.getPasscode())
        );
        if(!exam.getIsActive()){
            throw new NotFoundException("Exam not found with passcode: " + examResultCreateRequest.getPasscode());
        }
        if(exam.getEndTime().isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))){
            throw new NotFoundException("Exam has already finished");
        }

        examResult.setExam(exam);
        examResultRepository.save(examResult);
        ExamResultCreateResponse examCreateResponse = ExamResultMapper.INSTANCE.toExamResultCreateResponse(examResult);
        StudentJwtPayload studentJwtPayload = StudentMapper.INSTANCE.toStudentJwtPayload(examResult);
        JwtPayload jwtPayload = new JwtPayload(studentJwtPayload);
        examCreateResponse.setExamResultToken(jwtUtil.generateToken(jwtPayload).getAccess_token());
        examCreateResponse.setExamGetResponse(ExamMapper.INSTANCE.toExamGetResponse(exam));
        return examCreateResponse;
    }
    public List<ExamResultUpdateResponse> accessExam(ExamResultUpdateRequest examResultUpdateRequest, Long id) throws NotFoundException, BadRequestException {
        ExamResult examResult = examResultRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Exam result not found with id: " + id)
        );
        if(!Objects.equals(examResult.getExam().getExamId(), examResultUpdateRequest.getExamId())){
            throw new NotFoundException("Exam result not found with id: " + id);
        }
        if (examResult.getStartedAt() != null) {
            if (examResult.getStartedAt().isAfter(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))) {
                throw new BadRequestException("Exam has not started yet");
            }
        }
        if(examResult.getExam().getEndTime()!=null&&examResult.getExam().getEndTime().isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))){
            throw new BadRequestException("Exam has already finished");
        }
        if(!examResult.getExam().getIsActive()){
            throw new BadRequestException("Exam has not started yet");
        }
        if(examResult.getFinishedAt()!=null){
            throw new BadRequestException("Exam has already finished");
        }
        if(examResult.getStartedAt()==null){
            examResult.setStartedAt(examResultUpdateRequest.getStartedAt());
            examResultRepository.save(examResult);
        }
        List<Question> questions = questionRepository.findAllByExam_ExamId(examResult.getExam().getExamId());
        List<ExamResultDetail> examResultDetails = examResultDetailService.getExamResultDetailsByExamResultId(id);
        List<ExamResultUpdateResponse> examResultUpdateResponseList = QuestionMapper.INSTANCE.toExamResultUpdateResponseList(questions);
        for (ExamResultUpdateResponse examResultUpdateResponse : examResultUpdateResponseList) {
            for (ExamResultDetail examResultDetail : examResultDetails) {
                if (Objects.equals(examResultDetail.getQuestion().getQuestionId(), examResultUpdateResponse.getQuestionId())) {
                    examResultUpdateResponse.setExamResultDetailId(examResultDetail.getExamResultDetailId());
                    examResultUpdateResponse.setResponse(examResultDetail.getResponse());
                }
            }
        }
        System.out.println(id);
        return examResultUpdateResponseList;
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
        examResult.setFinishedAt(finishAt != null && finishAt.isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)) ? finishAt : LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

        // Save submission
        examResultRepository.save(examResult);
    }
}
