package com.team2.online_examination.dtos.requests;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ExamSubmitRequest {
    private LocalDateTime finish_at;
}
