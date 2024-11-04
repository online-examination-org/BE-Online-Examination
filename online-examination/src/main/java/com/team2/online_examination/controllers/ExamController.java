package com.team2.online_examination.controllers;

import com.team2.online_examination.annotations.Authorize;
import com.team2.online_examination.contexts.UserContext;
import com.team2.online_examination.dtos.JwtPayload;
import com.team2.online_examination.dtos.requests.ExamCreateRequest;
import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.services.ExamService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {
    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }
    @Authorize(roles = {"teacher"})
    @PostMapping("/add")
    public ResponseEntity<?> addExam(@RequestBody @Valid ExamCreateRequest examCreateRequest) {
        try {
            JwtPayload payload = UserContext.getJwtPayload();
            System.out.println(payload.getClaims().get("id"));
            this.examService.createExam(examCreateRequest, (Long) payload.getClaims().get("id"));
            return ResponseEntity.ok("Exam created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/update")
    public ResponseEntity<?> updateExam(@RequestBody @Valid ExamUpdateRequest examUpdateRequest, @RequestParam @NotNull(message = "Id is required") Long id) {
        try {
            this.examService.updateExam(examUpdateRequest, 1L,id);
            return ResponseEntity.ok("Exam updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
