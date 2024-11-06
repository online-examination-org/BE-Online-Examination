package com.team2.online_examination.mappers;

import com.team2.online_examination.dtos.requests.ExamResultCreateRequest;
import com.team2.online_examination.dtos.responses.ExamResultCreateResponse;
import com.team2.online_examination.dtos.responses.ExamResultUpdateResponse;
import com.team2.online_examination.models.ExamResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExamResultMapper {
    ExamResultMapper INSTANCE = Mappers.getMapper(ExamResultMapper.class);
    ExamResult toExamResult(ExamResultCreateRequest examResultCreateRequest);
    ExamResultCreateResponse toExamResultCreateResponse(ExamResult examResult);
}
