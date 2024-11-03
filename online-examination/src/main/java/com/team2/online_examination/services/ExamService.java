package com.team2.online_examination.services;

import com.team2.online_examination.dtos.requests.ExamCreateRequest;
import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.exceptions.NotFoundException;
import com.team2.online_examination.mappers.ExamMapper;
import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.Teacher;
import com.team2.online_examination.repositories.ExamRepository;
import com.team2.online_examination.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final TeacherRepository teacherRepository;

    public ExamService(ExamRepository examRepository, TeacherRepository teacherRepository) {
        this.examRepository = examRepository;
        this.teacherRepository = teacherRepository;
    }

    public void createExam(ExamCreateRequest examCreateRequest, Long teacherId) throws NotFoundException {
        Exam exam = ExamMapper.INSTANCE.toExam(examCreateRequest);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new NotFoundException("Teacher not found with id: " + teacherId)
        );
        exam.setTeacher(teacher);
        examRepository.save(exam);
    }
    public void updateExam(ExamUpdateRequest examUpdateRequest, Long teacherId,String passcode) throws NotFoundException {
        Exam updateExam = ExamMapper.INSTANCE.toExam(examUpdateRequest);
        Exam exam = examRepository.findByPasscode(passcode).orElseThrow(
                () -> new NotFoundException("Exam not found with passcode: " + passcode)
        );
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new NotFoundException("Teacher not found with id: " + teacherId)
        );
        if(!exam.getTeacher().getId().equals(teacherId)){
            throw new NotFoundException("Teacher not found");
        }
        exam.setDuration(updateExam.getDuration()!=null?updateExam.getDuration():exam.getDuration());
        exam.setStartTime(updateExam.getStartTime()!=null?updateExam.getStartTime():exam.getStartTime());
        exam.setEndTime(updateExam.getEndTime()!=null?updateExam.getEndTime():exam.getEndTime());
        exam.setTitle(updateExam.getTitle()!=null?updateExam.getTitle():exam.getTitle());
        exam.setIsActive(updateExam.getIsActive()!=null?updateExam.getIsActive():exam.getIsActive());
        exam.setDescription(updateExam.getDescription()!=null?updateExam.getDescription():exam.getDescription());
        examRepository.save(exam);
    }
}
