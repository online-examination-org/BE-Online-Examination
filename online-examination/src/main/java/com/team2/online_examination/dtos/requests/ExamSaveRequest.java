package com.team2.online_examination.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ExamSaveRequest {
    private Long exam_result_id;

    @NotBlank(message = "Question Id is required")
    private Long question_id;

    private String response;
}
