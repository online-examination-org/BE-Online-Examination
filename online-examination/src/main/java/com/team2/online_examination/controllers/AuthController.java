package com.team2.online_examination.controllers;

import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.services.TeacherService;
import com.team2.online_examination.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TeacherService teacherService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(TeacherService teacherService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.teacherService = teacherService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Teacher teacher) {
        Teacher registeredTeacher = teacherService.signup(teacher);
        return ResponseEntity.ok(registeredTeacher);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

