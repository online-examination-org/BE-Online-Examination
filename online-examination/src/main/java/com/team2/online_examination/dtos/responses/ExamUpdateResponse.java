package com.team2.online_examination.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamUpdateResponse {
    private Long examId;
    private String title;
    private String passcode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private String description;
    private Boolean isActive;
}
