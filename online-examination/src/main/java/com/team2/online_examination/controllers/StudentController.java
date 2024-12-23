package com.team2.online_examination.controllers;

import com.team2.online_examination.annotations.Authorize;
import com.team2.online_examination.contexts.StudentContext;
import com.team2.online_examination.contexts.UserContext;
import com.team2.online_examination.dtos.requests.ExamSaveRequest;
import com.team2.online_examination.dtos.requests.ExamSubmitRequest;
import com.team2.online_examination.exceptions.AuthenticationFailureException;
import com.team2.online_examination.exceptions.BadRequestException;
import com.team2.online_examination.exceptions.GeneralErrorResponse;
import com.team2.online_examination.models.ExamResult;
import com.team2.online_examination.services.ExamResultDetailService;
import com.team2.online_examination.services.ExamResultService;
import com.team2.online_examination.services.ExamService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.team2.online_examination.models.Exam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;
@RestController

@RequestMapping("/api/v1/students")
public class StudentController {

    private final ExamService examService;
    private final ExamResultService examResultService;
    private final ExamResultDetailService examResultDetailService;

    @Autowired
    public StudentController(
            ExamService examService,
            ExamResultService examResultService,
            ExamResultDetailService examResultDetailService
    ) {
        this.examService = examService;
        this.examResultService = examResultService;
        this.examResultDetailService = examResultDetailService;
    }

    //This function only get the exam metadata without question
    @GetMapping("/exam")
    public Optional<Exam> getExamByPasscode(@RequestParam("passcode") String passcode) {
        return this.examService.getExamByPasscode(passcode);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"student"})
    @PostMapping("/exam/save")
    public ResponseEntity<?> saveProgress(@RequestBody ExamSaveRequest examSaveRequest) {
        try {
            StudentContext studentContext = UserContext.getUserAs(StudentContext.class);
            if(studentContext == null){
                throw new BadRequestException("Student context is required");
            }
            Long examResultId = studentContext.getExamResultId();
            Long questionId = examSaveRequest.getQuestion_id();
            String response = examSaveRequest.getResponse();
            this.examResultDetailService.updateResponse(examResultId, questionId, response);

            return ResponseEntity.ok(null);
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(new GeneralErrorResponse(e.getReason()));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeneralErrorResponse(e.getMessage()));
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"student"})
    @PostMapping("/exam/submit")
    public ResponseEntity<?> submit(@RequestBody ExamSubmitRequest examSubmitRequest) {
        try {
            StudentContext studentContext = UserContext.getUserAs(StudentContext.class);
            if(studentContext == null){
                throw new BadRequestException("Student context is required");
            }
            Long examResultId = studentContext.getExamResultId();
            LocalDateTime finishAt = examSubmitRequest.getFinish_at();
            this.examResultService.submit(examResultId, finishAt);

            return ResponseEntity.ok(null);

        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(new GeneralErrorResponse(e.getReason()));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeneralErrorResponse(e.getMessage()));
        }
    }


}
