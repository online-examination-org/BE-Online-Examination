package com.team2.online_examination.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {

    String questionText;

    String questionType;

    String answer;

    Map<String,String> choices;
}
