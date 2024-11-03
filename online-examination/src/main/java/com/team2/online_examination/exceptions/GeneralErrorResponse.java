package com.team2.online_examination.exceptions;

import lombok.Data;

@Data
public class GeneralErrorResponse {
    private String message;
    public GeneralErrorResponse() {
        this.message = "An unexpected error occurred";
    }

    // Constructor with message parameter
    public GeneralErrorResponse(String message) {
        this.message = (message == null || message.isEmpty()) ? "An unexpected error occurred" : message;
    }
}
