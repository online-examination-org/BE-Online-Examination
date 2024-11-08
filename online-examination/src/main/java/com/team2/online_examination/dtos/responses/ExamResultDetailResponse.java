package com.team2.online_examination.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDetailResponse {
    private String questionText;
    private String questionType;
    private String answer;
    private Map<String, String> choices;
    private String response;
    private boolean isCorrect;
}
