package com.team2.online_examination.services;

import com.team2.online_examination.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.team2.online_examination.models.Exam;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    @Autowired
    public ExamService(
            ExamRepository examRepository
    ) {
        this.examRepository = examRepository;
    }

    public List<Exam> getListExamByTeacherId(Long teacherId) {
        return examRepository.findByTeacher_Id(teacherId);
    }

    public Optional<Exam> getExamByPasscode(String passcode) {
        return examRepository.findByPasscode(passcode);
    }
}



