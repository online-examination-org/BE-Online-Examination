package com.team2.online_examination.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TeacherJwtPayload {
    private Long id;
    private String name;
    private String email;
}
