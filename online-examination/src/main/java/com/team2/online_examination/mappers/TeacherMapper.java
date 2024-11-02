package com.team2.online_examination.mappers;

import com.team2.online_examination.dtos.TeacherJwtPayload;
import com.team2.online_examination.dtos.requests.TeacherCreateRequest;
import com.team2.online_examination.dtos.responses.TeacherCreateResponse;
import com.team2.online_examination.models.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {
    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher toTeacher(TeacherCreateRequest request);
    TeacherCreateResponse toTeacherCreateResponse(Teacher teacher);
    TeacherJwtPayload toTeacherJwtPayload(Teacher teacher);
}
