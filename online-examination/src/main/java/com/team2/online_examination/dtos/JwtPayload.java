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
                System.out.println(e.getMessage());
            }
        }
    }

    public <T> T toObject(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                Field field;
                try {
                    field = clazz.getDeclaredField(entry.getKey());
                    field.setAccessible(true);

                    Object value = entry.getValue();
                    if (value != null && !field.getType().isInstance(value)) {
                        // Convert the value if it’s a different type than the field
                        value = convertType(value, field.getType());
                    }

                    field.set(instance, value);
                } catch (NoSuchFieldException e) {
                    System.out.println("Field " + entry.getKey() + " not found in " + clazz.getSimpleName());
                }
            }
            return instance;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Object convertType(Object value, Class<?> targetType) {
        if (value instanceof Integer) {
            if (targetType == Long.class || targetType == long.class) {
                return ((Integer) value).longValue();
            } else if (targetType == Double.class || targetType == double.class) {
                return ((Integer) value).doubleValue();
            } else if (targetType == Float.class || targetType == float.class) {
                return ((Integer) value).floatValue();
            }
        } else if (value instanceof Long) {
            if (targetType == Integer.class || targetType == int.class) {
                return ((Long) value).intValue();
            }
        } else if (value instanceof Double) {
            if (targetType == Float.class || targetType == float.class) {
                return ((Double) value).floatValue();
            }
        }
        return value;
    }
}
