package com.onkonfeton.adapter;

import java.util.UUID;

public class UuidAdapter implements JsonDeserializer<UUID>, JsonSerializer<UUID> {

    @Override
    public UUID deserialize(Object value) {
        return UUID.fromString(value.toString());
    }

    @Override
    public String serialize(Object uuid) {
        return "\"" + uuid.toString() + "\"";
    }
}
