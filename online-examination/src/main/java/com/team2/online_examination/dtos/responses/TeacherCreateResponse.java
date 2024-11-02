package com.team2.online_examination.dtos.responses;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class TeacherCreateResponse {
    private Long id;
    private String name;
    private String email;
    private Date createdAt;
    private LocalDateTime updatedAt;
}
