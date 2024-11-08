package com.team2.online_examination.controllers;

import com.team2.online_examination.annotations.Authorize;
import com.team2.online_examination.contexts.StudentContext;
import com.team2.online_examination.contexts.TeacherContext;
import com.team2.online_examination.contexts.UserContext;
import com.team2.online_examination.dtos.requests.ExamResultCreateRequest;
import com.team2.online_examination.dtos.requests.ExamResultUpdateRequest;
import com.team2.online_examination.exceptions.BadRequestException;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.ExamResult;
import com.team2.online_examination.services.ExamResultService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-results")
public class ExamResultController {
    private final ExamResultService examResultService;

    @Autowired
    public ExamResultController(ExamResultService examResultService) {
        this.examResultService = examResultService;
    }

    @PostMapping("add")
    public ResponseEntity<?> addExamResult(@RequestBody ExamResultCreateRequest examResultCreateRequest) {
        // Add exam result
        try{
            return ResponseEntity.ok(examResultService.addExamResult(examResultCreateRequest));
        }
        catch (Exception e){
            if(e instanceof NotFoundException){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            else if(e instanceof BadRequestException){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"student"})
    @PutMapping("/update")
    public ResponseEntity<?> updateExamResult(@RequestBody ExamResultUpdateRequest examResultUpdateRequest) {
        // Update exam result
        try{
            StudentContext studentContext = UserContext.getUserAs(StudentContext.class);
            if(studentContext == null){
                return ResponseEntity.badRequest().body("Student context is required");
            }
            return ResponseEntity.ok(examResultService.accessExam(examResultUpdateRequest, studentContext.getExamResultId()));
        }
        catch (Exception e){
            if(e instanceof NotFoundException){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            else if(e instanceof BadRequestException){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"teacher"})
    @GetMapping("/{examId}")
    public ResponseEntity<?> getExamResult(@PathVariable Long examId) {
        List<ExamResult> examResultList=  this.examResultService.getAllExamResultByExamId(examId);
        return ResponseEntity.ok(examResultList);
    }
}
