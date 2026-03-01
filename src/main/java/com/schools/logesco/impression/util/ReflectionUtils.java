package com.schools.logesco.impression.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static String getFieldValue(Object obj, String fieldPath) {
        try {
            String[] parts = fieldPath.split("\\.");
            Object value = obj;

            for (String part : parts) {
                Field f = value.getClass().getDeclaredField(part);
                f.setAccessible(true);
                value = f.get(value);
                if (value == null) return "";
            }

            return value.toString();

        } catch (Exception e) {
            return "";
        }
    }
}
