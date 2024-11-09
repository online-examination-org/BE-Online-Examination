package com.team2.online_examination.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team2.online_examination.models.Teacher;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
