package com.team2.online_examination.contexts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StudentContext {
    private Long examResultId;
    private final String role = "student";
}
