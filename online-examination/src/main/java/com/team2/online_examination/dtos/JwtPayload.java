package com.team2.online_examination.dtos;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Getter
public class JwtPayload {

    private final Map<String, Object> claims = new HashMap<>();

    public JwtPayload() {
    }

    public JwtPayload(Object input) {
        addClaimsFromObject(input);
    }

    public void addClaim(String key, Object value) {
        claims.put(key, value);
    }

    private void addClaimsFromObject(Object input) {
        if (input == null) return;

        Field[] fields = input.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);  // Allow access to private fields
            try {
                Object value = field.get(input);  // Get the value of the field
                claims.put(field.getName(), value);  // Add field name and value to claims
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
