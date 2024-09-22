package com.onkonfeton.adapter;

import java.time.OffsetDateTime;

public class DefaultOffsetDateTimeAdapter implements JsonDeserializer<OffsetDateTime>, JsonSerializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(Object value) {
        return OffsetDateTime.parse(value.toString());
    }

    @Override
    public String serialize(Object offsetDateTime) {
        return "\"" + offsetDateTime.toString() + "\"";
    }
}
