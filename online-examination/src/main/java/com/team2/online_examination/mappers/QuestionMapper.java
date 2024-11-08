package com.team2.online_examination.mappers;

import com.team2.online_examination.dtos.requests.QuestionCreateRequest;
import com.team2.online_examination.dtos.requests.QuestionUpdateRequest;
import com.team2.online_examination.dtos.requests.TeacherCreateRequest;
import com.team2.online_examination.dtos.responses.QuestionResponse;
import com.team2.online_examination.dtos.responses.TeacherCreateResponse;
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

    QuestionCreateRequest toQuestionCreateRequest (Question question);

    QuestionResponse toQuestionResponse(Question question);

    Question toQuestion(QuestionCreateRequest request);

    Question toQuestion(QuestionUpdateRequest request);
}
