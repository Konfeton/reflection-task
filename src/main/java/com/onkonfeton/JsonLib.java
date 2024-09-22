package com.onkonfeton;

import com.onkonfeton.adapter.DefaultLocalDateAdapter;
import com.onkonfeton.adapter.DefaultOffsetDateTimeAdapter;
import com.onkonfeton.adapter.JsonDeserializer;
import com.onkonfeton.adapter.JsonSerializer;
import com.onkonfeton.adapter.JsonTypeAdapter;
import com.onkonfeton.adapter.UuidAdapter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class JsonLib {

    private final JsonParser jsonParser;
    private final JsonMapper jsonMapper;

    private final ObjectToJsonConverter toJsonConverter;

    public JsonLib() {
        this.jsonParser = new JsonParser();
        this.jsonMapper = new JsonMapper();
        this.toJsonConverter = new ObjectToJsonConverter();
        init();
    }

    private void init() {
        registerTypeAdapter(UUID.class, new UuidAdapter());
        registerTypeAdapter(LocalDate.class, new DefaultLocalDateAdapter());
        registerTypeAdapter(OffsetDateTime.class, new DefaultOffsetDateTimeAdapter());
    }

    public <T> T deserialize(String json, Class<T> clazz){
        if (json == null || json.isEmpty() || json.isBlank()){
            throw new RuntimeException("Invalid JSON " + json);
        }

        Map<String, Object> parsed = jsonParser.parse(json);
        T deserialized = jsonMapper.assignValues(parsed, clazz);
        return deserialized;
    }

    public String serialize(Object src){
        try {
            return toJsonConverter.toJson(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void registerTypeAdapter(Class<?> clazz, JsonTypeAdapter adapter) {
        if (adapter instanceof JsonDeserializer<?> deserializer) {
            jsonMapper.addTypeAdapter(clazz, deserializer);
        }
        if (adapter instanceof JsonSerializer<?> serializer){
            toJsonConverter.addTypeAdapter(clazz, serializer);
        }
    }
}
