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

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {
    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"teacher"})
    @PostMapping("/add")
    public ResponseEntity<?> addExam(@RequestBody @Valid ExamCreateRequest examCreateRequest) {
        try {
            JwtPayload payload = UserContext.getJwtPayload();
            Integer teacherId = (Integer) payload.getClaims().get("id");
            Long idLong = teacherId.longValue();
            this.examService.createExam(examCreateRequest, idLong);
            return ResponseEntity.ok("Exam created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"teacher"})
    @PutMapping("/update")
    public ResponseEntity<?> updateExam(@RequestBody ExamUpdateRequest examUpdateRequest, @RequestParam("id") Long id) {
        try {
            if(id == null){
                return ResponseEntity.badRequest().body("Id is required");
            }
            JwtPayload payload = UserContext.getJwtPayload();
            Integer teacherId = (Integer) payload.getClaims().get("id");
            Long idLong = teacherId.longValue();
            this.examService.updateExam(examUpdateRequest, idLong,id);
            return ResponseEntity.ok("Exam updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
