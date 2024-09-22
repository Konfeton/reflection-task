package com.onkonfeton.adapter;

public interface JsonSerializer<T> extends JsonTypeAdapter {
    String serialize(Object o);
}
