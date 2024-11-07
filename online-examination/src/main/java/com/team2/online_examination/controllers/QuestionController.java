package com.team2.online_examination.controllers;

import java.util.ArrayList;

import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.dtos.requests.QuestionCreateRequest;
import com.team2.online_examination.dtos.requests.QuestionUpdateRequest;
import com.team2.online_examination.dtos.requests.TeacherCreateRequest;
import com.team2.online_examination.dtos.responses.TeacherCreateResponse;
import com.team2.online_examination.exceptions.EmailExistedException;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.exceptions.QuestionExistedException;
import com.team2.online_examination.mappers.QuestionMapper;
import com.team2.online_examination.mappers.TeacherMapper;
import com.team2.online_examination.models.Question;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.services.QuestionService;
import com.team2.online_examination.services.TeacherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/view")
    public ResponseEntity<?> findQuestion(@RequestParam(defaultValue = "0") long exam_id) {
        try {
            return new ResponseEntity<>(questionService.findAllByExamId(exam_id), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody List<QuestionCreateRequest> questionCreateRequests) {
        try {
            return ResponseEntity.ok(questionService.saveAll(questionCreateRequests));
        }
        catch (QuestionExistedException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateQuestion(@RequestBody @Valid QuestionUpdateRequest questionUpdateRequest, @RequestParam @NotNull(message = "Id is required") Long id, @RequestParam @NotNull(message = "exam_id is required") Long exam_id) {
        try {
            this.questionService.updateQuestion(questionUpdateRequest, id,exam_id);
            return ResponseEntity.ok("Question updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
