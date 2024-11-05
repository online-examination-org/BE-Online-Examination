package com.team2.online_examination.controllers;


import com.team2.online_examination.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.team2.online_examination.models.Exam;
import java.util.Optional;

import java.util.List;
@RestController

@RequestMapping("/api/v1/students")
public class StudentController {

    private final ExamService examService;

    @Autowired
    public StudentController(ExamService examService) {
        this.examService = examService;
    }

    //This function only get the exam metadata without question
    @GetMapping("/exam")
    public Optional<Exam> getExamByPasscode(@RequestParam("passcode") String passcode) {
        return this.examService.getExamByPasscode(passcode);
    }

}
