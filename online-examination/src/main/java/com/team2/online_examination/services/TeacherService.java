package com.team2.online_examination.services;

import com.team2.online_examination.dtos.JwtPayload;
import com.team2.online_examination.dtos.TeacherJwtPayload;
import com.team2.online_examination.dtos.JwtToken;
import com.team2.online_examination.exceptions.AuthenticationFailureException;
import com.team2.online_examination.exceptions.EmailExistedException;
import com.team2.online_examination.mappers.TeacherMapper;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.repositories.TeacherRepository;
import com.team2.online_examination.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public TeacherService(
            TeacherRepository teacherRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public Teacher signup(Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

        Optional<Teacher> findTeacher = teacherRepository.findByEmail(teacher.getEmail());

        if (findTeacher.isPresent()) {
            throw new EmailExistedException("Email is existed!");
        }

        return teacherRepository.save(teacher);
    }

    public Teacher findByEmail(String email) {

        return teacherRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    public Teacher findById(Long id) {

        return teacherRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    public JwtToken authentication(String email, String password) {
        try {
            // Verify password
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Check user existence
            UserDetails authTeacher = (UserDetails) auth.getPrincipal();
            Teacher teacher = teacherRepository
                    .findByEmail(authTeacher.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate tokens
            TeacherJwtPayload teacherJwtPayload = TeacherMapper.INSTANCE.toTeacherJwtPayload(teacher);
            JwtPayload payload = new JwtPayload(teacherJwtPayload);
            JwtToken token = jwtUtil.generateToken(payload);

            // Add role
            token.setRole(teacherJwtPayload.getRole());

            return token;

        } catch (AuthenticationException e) {
            throw new AuthenticationFailureException("Invalid email or password");
        }
    }
}
