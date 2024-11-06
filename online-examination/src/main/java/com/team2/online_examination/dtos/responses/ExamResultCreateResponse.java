package com.team2.online_examination.dtos.responses;

import com.team2.online_examination.models.Exam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultCreateResponse {
    private Long examResultId;
    private ExamGetResponse examGetResponse;
    private String name;
    private String email;

}
