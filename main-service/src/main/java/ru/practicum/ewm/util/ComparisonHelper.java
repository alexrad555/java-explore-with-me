package ru.practicum.ewm.util;

import java.time.LocalDateTime;

public class ComparisonHelper {

    public static String getValue(String newValue, String oldValue) {
        return (String) compareAndGetValue(newValue, oldValue);
    }

    public static Long getValue(Long newValue, Long oldValue) {
        return (Long) compareAndGetValue(newValue, oldValue);
    }

    public static LocalDateTime getValue(LocalDateTime newValue, LocalDateTime oldValue) {
        return (LocalDateTime) compareAndGetValue(newValue, oldValue);
    }

    public static Boolean getValue(Boolean newValue, Boolean oldValue) {
        return (Boolean) compareAndGetValue(newValue, oldValue);
    }

    public static Integer getValue(Integer newValue, Integer oldValue) {
        return (Integer) compareAndGetValue(newValue, oldValue);
    }

    private static Object compareAndGetValue(Object newValue, Object oldValue) {
        return newValue != null ? newValue : oldValue;
    }
}
