package com.team2.online_examination.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
