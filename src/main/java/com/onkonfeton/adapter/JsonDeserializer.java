package com.onkonfeton.adapter;

public interface JsonDeserializer<T> extends JsonTypeAdapter {
    T deserialize(Object value);
}
