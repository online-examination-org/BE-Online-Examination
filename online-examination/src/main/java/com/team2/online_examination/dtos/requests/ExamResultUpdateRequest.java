package com.team2.online_examination.dtos.requests;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultUpdateRequest {
    @NotNull(message = "Exam id is required")
    private Long examId;
    @NotNull(message = "Start time is required")
    private LocalDateTime startedAt;

}
