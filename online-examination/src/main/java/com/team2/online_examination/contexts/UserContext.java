package com.team2.online_examination.contexts;

import com.team2.online_examination.dtos.JwtPayload;

public class UserContext {
    private static final ThreadLocal<JwtPayload> userContext = new ThreadLocal<>();

    public static void setJwtPayload(JwtPayload payload) {
        userContext.set(payload);
    }

    public static JwtPayload getJwtPayload() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }
}
