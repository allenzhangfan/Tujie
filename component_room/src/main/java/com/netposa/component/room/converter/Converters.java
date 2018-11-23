package com.netposa.component.room.converter;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static boolean getBooleanFromString(String value) {
        return "1".equals(value);
    }

    @TypeConverter
    public static String getStringFromBoolean(boolean value) {
        return value ? "1" : "0";
    }
}
