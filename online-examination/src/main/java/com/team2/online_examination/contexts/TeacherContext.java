package com.team2.online_examination.contexts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TeacherContext {
    private Long id;
    private String name;
    private String email;
    private final String role = "teacher";
}
