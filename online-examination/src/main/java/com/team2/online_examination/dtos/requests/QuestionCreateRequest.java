package com.team2.online_examination.dtos.requests;

import com.team2.online_examination.models.Question;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {

    @NotBlank(message = "Exam id is required")
    Long exam_id;

    @NotBlank(message = "Question Text is required")
    String questionText;

    @NotBlank(message = "Question Type is required")
    String questionType;

    @NotBlank(message = "Answer is required")
    String answer;

    @NotBlank(message = "Choices is required")
    Map<String,String> choices;

}
