package com.team2.online_examination.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class QuestionCreateRequest {

    @NotBlank(message = "Exam id is required")
    long exam_id;

    @NotBlank(message = "Question Text is required")
    String questionText;

    @NotBlank(message = "Question Type is required")
    String questionType;

    @NotBlank(message = "Answer is required")
    String answer;

    @NotBlank(message = "Choices is required")
    String choices;

}
