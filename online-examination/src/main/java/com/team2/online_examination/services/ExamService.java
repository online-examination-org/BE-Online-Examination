package com.team2.online_examination.services;

import com.github.f4b6a3.uuid.UuidCreator;
import com.team2.online_examination.dtos.requests.ExamCreateRequest;
import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.dtos.responses.ExamCreateResponse;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.exceptions.BadRequest;
import com.team2.online_examination.mappers.ExamMapper;
import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.repositories.ExamRepository;
import com.team2.online_examination.repositories.QuestionRepository;
import com.team2.online_examination.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final TeacherRepository teacherRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, TeacherRepository teacherRepository, QuestionRepository questionRepository ) {
        this.examRepository = examRepository;
        this.teacherRepository = teacherRepository;
        this.questionRepository = questionRepository;
    }

    public String randomCode() {
        return UuidCreator.getTimeBased().toString().substring(0, 6);
    }

    public ExamCreateResponse createExam(ExamCreateRequest examCreateRequest, Long teacherId) throws NotFoundException {
        Exam exam = ExamMapper.INSTANCE.toExam(examCreateRequest);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new NotFoundException("Teacher not found with id: " + teacherId));
        exam.setPasscode(randomCode());
        exam.setTeacher(teacher);
        exam.setIsActive(true);
        examRepository.save(exam);
        return ExamMapper.INSTANCE.toExamCreateResponse(exam);
    }

    public void updateExam(ExamUpdateRequest examUpdateRequest, Long teacherId, Long id) throws NotFoundException {
        Exam updateExam = ExamMapper.INSTANCE.toExam(examUpdateRequest);
        Exam exam = examRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Exam not found with id: " + id));
        if (exam.getStartTime().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("Exam is already started");
        }
        if (!exam.getTeacher().getId().equals(teacherId)) {
            throw new NotFoundException("Teacher not found");
        }
        exam.setDuration(updateExam.getDuration() != null ? updateExam.getDuration() : exam.getDuration());
        exam.setStartTime(updateExam.getStartTime() != null ? updateExam.getStartTime() : exam.getStartTime());
        exam.setEndTime(updateExam.getEndTime() != null ? updateExam.getEndTime() : exam.getEndTime());
        exam.setTitle(updateExam.getTitle() != null ? updateExam.getTitle() : exam.getTitle());
        exam.setIsActive(updateExam.getIsActive() != null ? updateExam.getIsActive() : exam.getIsActive());
        exam.setDescription(updateExam.getDescription() != null ? updateExam.getDescription() : exam.getDescription());
        examRepository.save(exam);
    }

    public List<Exam> getListExamByTeacherId(Long teacherId) {
        return examRepository.findByTeacher_Id(teacherId);
    }

    public Optional<Exam> getExamByPasscode(String passcode) {
        return examRepository.findByPasscode(passcode);
    }

    @Transactional
    public void deleteDraftExam(Long examId,Long teacherId) {
        //check if teacher create this exam
        Exam exam = examRepository.findByExamIdAndTeacher_Id(examId,teacherId).orElseThrow(
                () -> new NotFoundException("Exam not found with id: " + examId)
        );
        Boolean is_active = exam.getIsActive();
        //only delete the draft exam
        if (is_active) {
            throw new BadRequest("Can't delete an active exam");
        }
        questionRepository.deleteAllByExam_ExamId(examId);
        examRepository.deleteByExamId(examId);

    }

}
