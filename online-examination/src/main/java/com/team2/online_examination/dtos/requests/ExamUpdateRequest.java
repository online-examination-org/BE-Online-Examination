package com.team2.online_examination.dtos.requests;

import jakarta.validation.constraints.NotBlank;
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
public class ExamUpdateRequest {
    //@NotBlank(message = "Title is required")
    private String title;
    //@NotNull(message = "Start time is required")
    private LocalDateTime start_time;
    //@NotNull(message = "End time is required")
    private LocalDateTime end_time;
    //@NotNull(message = "Passcode is required")
    private Integer duration;
    //@NotBlank(message = "Description is required")
    private String description;
    private Boolean isActive;
}