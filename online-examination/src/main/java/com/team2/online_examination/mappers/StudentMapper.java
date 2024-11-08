package com.team2.online_examination.mappers;

import com.team2.online_examination.dtos.StudentJwtPayload;
import com.team2.online_examination.models.ExamResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    StudentJwtPayload toStudentJwtPayload(ExamResult examResult);
}
