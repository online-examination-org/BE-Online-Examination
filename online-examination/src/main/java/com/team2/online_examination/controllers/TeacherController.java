package com.team2.online_examination.controllers;

import com.team2.online_examination.annotations.Authorize;
import com.team2.online_examination.contexts.TeacherContext;
import com.team2.online_examination.contexts.UserContext;
import com.team2.online_examination.dtos.JwtPayload;
import com.team2.online_examination.dtos.TeacherJwtPayload;
import com.team2.online_examination.dtos.requests.TeacherLoginRequest;
import com.team2.online_examination.dtos.requests.TeacherCreateRequest;
import com.team2.online_examination.exceptions.GeneralErrorResponse;
import com.team2.online_examination.dtos.JwtToken;
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
import org.springframework.web.server.ResponseStatusException;
import com.team2.online_examination.models.Exam;

import java.util.List;
@RestController

@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final ExamService examService;

    @Autowired
    public TeacherController(TeacherService teacherService,ExamService examService) {
        this.teacherService = teacherService;
        this.examService = examService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody TeacherCreateRequest teacherCreateRequest) {
        try {
            Teacher teacher = TeacherMapper.INSTANCE.toTeacher(teacherCreateRequest);
            Teacher registeredTeacher = teacherService.signup(teacher);
            TeacherCreateResponse teacherCreateResponse = TeacherMapper.INSTANCE.toTeacherCreateResponse(registeredTeacher);

            return ResponseEntity.ok(teacherCreateResponse);
        } catch (EmailExistedException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GeneralErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeneralErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TeacherLoginRequest teacherLoginRequest) {
        try {
            String email = teacherLoginRequest.getEmail();
            String password = teacherLoginRequest.getPassword();

            JwtToken jwtToken = teacherService.authentication(email, password);

            return ResponseEntity.ok(jwtToken);

        } catch (AuthenticationFailureException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new GeneralErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeneralErrorResponse(e.getMessage()));
        }
    }

    @Authorize(roles = {"teacher"})
    @GetMapping("/exams")
    public ResponseEntity<?> getExamsByTeacherId() {
        TeacherContext teacher = UserContext.getUserAs(TeacherContext.class);
        Long teacher_id= teacher.getId();
        List <Exam> exams=  this.examService.getListExamByTeacherId(teacher_id);
        return ResponseEntity.ok(exams);
    }

    @Authorize(roles = {"teacher"})
    @PostMapping("/authorize")
    public ResponseEntity<?> authorize () {
        try {
            TeacherContext teacherContext = UserContext.getUserAs(TeacherContext.class);
            if (teacherContext == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new GeneralErrorResponse("Invalid token"));
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeneralErrorResponse(e.getMessage()));
        }
    }

    @Authorize(roles = {"teacher"})
    @GetMapping("/info")
    public ResponseEntity<?> getInfo () {
        try {
            // Check context
            TeacherContext teacherContext = UserContext.getUserAs(TeacherContext.class);
            if (teacherContext == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new GeneralErrorResponse("Invalid token"));
            }

            // Find and map entity
            Teacher teacher = teacherService.findById(teacherContext.getId());
            TeacherCreateResponse teacherResponse = TeacherMapper.INSTANCE.toTeacherCreateResponse(teacher);

            return ResponseEntity.ok(teacherResponse);

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

