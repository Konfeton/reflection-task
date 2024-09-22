package com.onkonfeton.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DefaultLocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate deserialize(Object value) {
        return LocalDate.parse(value.toString(), formatter);
    }


    @Override
    public String serialize(Object localDate) {
        return "\"" + localDate.toString() + "\"";
    }
}
