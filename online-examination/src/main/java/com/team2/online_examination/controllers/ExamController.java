package com.team2.online_examination.controllers;

import com.team2.online_examination.annotations.Authorize;
import com.team2.online_examination.contexts.TeacherContext;
import com.team2.online_examination.contexts.UserContext;
import com.team2.online_examination.dtos.requests.ExamCreateRequest;
import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.exceptions.BadRequestException;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.services.ExamService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
    @SecurityRequirement(name = "Bearer Authentication")
    @Authorize(roles = {"teacher"})
    @PostMapping("/add")
    public ResponseEntity<?> addExam(@RequestBody @Valid ExamCreateRequest examCreateRequest) {
        try {
            TeacherContext teacherContext = UserContext.getUserAs(TeacherContext.class);
            if(teacherContext == null){
                return ResponseEntity.badRequest().body("Teacher context is required");
            }
            return ResponseEntity.ok(this.examService.createExam(examCreateRequest, teacherContext.getId()));
        } catch (Exception e) {
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
    @PutMapping("/update")
    public ResponseEntity<?> updateExam(@RequestBody ExamUpdateRequest examUpdateRequest, @RequestParam("id") Long id) {
        try {
            if(id == null){
                return ResponseEntity.badRequest().body("Id is required");
            }
            TeacherContext teacherContext = UserContext.getUserAs(TeacherContext.class);
            if(teacherContext == null){
                return ResponseEntity.badRequest().body("Teacher context is required");
            }
            this.examService.updateExam(examUpdateRequest, teacherContext.getId(),id);
            return ResponseEntity.ok("Exam updated successfully");
        } catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            else if(e instanceof BadRequestException){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Authorize(roles = {"teacher"})
    @DeleteMapping("/{examId}")
    public ResponseEntity<?> deleteDraftExam(@PathVariable Long examId) {
        try {
            TeacherContext teacher = UserContext.getUserAs(TeacherContext.class);
            Long teacherId = teacher.getId();
            this.examService.deleteDraftExam(examId, teacherId);
            return ResponseEntity.ok("Exam deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
