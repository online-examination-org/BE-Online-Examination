package com.team2.online_examination.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultCreateRequest {
    @NotNull(message = "Exam id is required")
    private Long examId;
    @NotNull(message = "Student is required")
    private String studentId;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
}
