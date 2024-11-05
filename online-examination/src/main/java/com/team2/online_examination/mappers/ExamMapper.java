package com.team2.online_examination.mappers;

import com.team2.online_examination.dtos.requests.ExamCreateRequest;
import com.team2.online_examination.dtos.requests.ExamUpdateRequest;
import com.team2.online_examination.dtos.responses.ExamCreateResponse;
import com.team2.online_examination.dtos.responses.ExamGetResponse;
import com.team2.online_examination.models.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExamMapper {
    ExamMapper INSTANCE = Mappers.getMapper(ExamMapper.class);

    Exam toExam(ExamCreateRequest request);
    ExamCreateResponse toExamCreateResponse(Exam exam);

    Exam toExam(ExamUpdateRequest examUpdateRequest);
    ExamGetResponse toExamGetResponse(Exam exam);
}
