package com.team2.online_examination.controllers;

import com.team2.online_examination.dtos.requests.ExamCreateRequest;
import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.services.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addExam(@RequestBody @Valid ExamCreateRequest examCreateRequest) {
        try {
            this.examService.createExam(examCreateRequest, 1L);
            return ResponseEntity.ok("Exam created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateExam(@RequestBody @Valid ExamUpdateRequest examUpdateRequest, @RequestParam String passcode) {
        try {
            this.examService.updateExam(examUpdateRequest, 1L,passcode);
            return ResponseEntity.ok("Exam updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
