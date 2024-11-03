package com.team2.online_examination.dtos.responses;

import lombok.Data;

@Data
public class JwtToken {
    private String access_token;
    private String refresh_token;
    private String role;
}
