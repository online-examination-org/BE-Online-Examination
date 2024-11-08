package com.team2.online_examination.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StudentJwtPayload {
    private Long examResultId;
    private final String role = "student";
}
