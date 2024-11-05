package com.team2.online_examination.mappers;

import com.team2.online_examination.dtos.responses.ExamResultUpdateResponse;
import com.team2.online_examination.models.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    ExamResultUpdateResponse toExamResultUpdateResponse(Question question);
    List<ExamResultUpdateResponse> toExamResultUpdateResponseList(List<Question> questions);

}
