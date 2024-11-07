package com.team2.online_examination.dtos.requests;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ExamSubmitRequest {
    private Long exam_result_id;
    private LocalDateTime finish_at;
}
