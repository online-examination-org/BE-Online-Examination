package com.team2.online_examination.controllers;

import com.team2.online_examination.dtos.requests.ExamResultCreateRequest;
import com.team2.online_examination.dtos.requests.ExamResultUpdateRequest;
import com.team2.online_examination.services.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateExamResult(@RequestBody ExamResultUpdateRequest examResultUpdateRequest, @RequestParam("id") Long id) {
        // Update exam result
        try{
            if(id == null){
                return ResponseEntity.badRequest().body("Id is required");
            }
            return ResponseEntity.ok(examResultService.accessExam(examResultUpdateRequest, id));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
