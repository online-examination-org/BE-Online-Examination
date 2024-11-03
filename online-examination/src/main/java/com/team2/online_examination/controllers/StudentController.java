package com.team2.online_examination.controllers;

import com.team2.online_examination.dtos.requests.TeacherLoginRequest;
import com.team2.online_examination.dtos.requests.TeacherCreateRequest;
import com.team2.online_examination.dtos.responses.GeneralErrorResponse;
import com.team2.online_examination.dtos.responses.JwtToken;
import com.team2.online_examination.dtos.responses.TeacherCreateResponse;
import com.team2.online_examination.exceptions.AuthenticationFailureException;
import com.team2.online_examination.exceptions.EmailExistedException;
import com.team2.online_examination.mappers.TeacherMapper;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.services.TeacherService;
import com.team2.online_examination.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/exam")
    public Optional<Exam> getExamByPasscode() {
        String passcode="2003";
        return this.examService.getExamByPasscode(passcode);
    }

}
