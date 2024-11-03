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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
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
    @PostMapping("/authorize")
    public ResponseEntity<?> authorize () {
        try {
//            JwtPayload payload = UserContext.getJwtPayload();
            TeacherContext teacher = UserContext.getUserAs(TeacherContext.class);
            return ResponseEntity.ok(teacher);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeneralErrorResponse(e.getMessage()));
        }
    }
}

