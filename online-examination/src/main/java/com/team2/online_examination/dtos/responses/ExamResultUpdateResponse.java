package com.team2.online_examination.dtos.responses;

import com.team2.online_examination.models.Exam;
import com.team2.online_examination.models.Question;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultUpdateResponse {
    private Long questionId;
    private String questionText;
    private String questionType;
    private String choices;
}
