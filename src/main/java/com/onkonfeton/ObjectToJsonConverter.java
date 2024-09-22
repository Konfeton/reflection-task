package com.onkonfeton;


import com.onkonfeton.adapter.JsonSerializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectToJsonConverter {

    private static final String OBJECT_START = "{";
    private static final String OBJECT_END = "}";
    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String COMMA = ",";
    private static final String COLON = ":";
    private static final String QUOTE = "\"";


    private final Map<Class<?>, JsonSerializer<?>> typeAdapters = new HashMap<>();

    public String toJson(Object src) throws Exception {
        StringBuilder json = new StringBuilder();
        Class<?> clazz = src.getClass();
        Field[] fields = clazz.getDeclaredFields();

        json.append(OBJECT_START);

        List<String> jsonElements = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);

            Object value = field.get(src);
            String name = field.getName();
            String jsonValue = handleValue(value);

            jsonElements.add(QUOTE + name + QUOTE + COLON + jsonValue);
        }

        json.append(String.join(",", jsonElements));
        json.append(OBJECT_END);

        return json.toString();
    }

    private String handleValue(Object value) throws Exception {
        if (value == null) {
            return null;
        }else if(typeAdapters.containsKey(value.getClass())){
            JsonSerializer<?> jsonSerializer = typeAdapters.get(value.getClass());
            return jsonSerializer.serialize(value);
        }else if (value instanceof String) {
            return QUOTE + value.toString() + QUOTE;
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value.getClass().isArray()) {
            return handleArray(value);
        } else if (Collection.class.isAssignableFrom(value.getClass())){
            return handleCollection((Collection<?>)value);
        }else if(Map.class.isAssignableFrom(value.getClass())) {
            return handleMap((Map<?, ?>) value);
        }else{
            return toJson(value);
        }
    }

    private String handleArray(Object array) throws Exception {
        int length = java.lang.reflect.Array.getLength(array);
        List<String> elements = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Object element = java.lang.reflect.Array.get(array, i);
            elements.add(handleValue(element));
        }
        return ARRAY_START + String.join(",", elements) + ARRAY_END;
    }

    private String handleCollection(Collection<?> collection) throws Exception {
        StringBuilder collectionJson = new StringBuilder("[");
        Iterator<?> iterator = collection.iterator();

        while (iterator.hasNext()) {
            Object element = iterator.next();
            collectionJson.append(handleValue(element));
            if (iterator.hasNext()) {
                collectionJson.append(",");
            }
        }

        collectionJson.append("]");
        return collectionJson.toString();
    }

    private String handleMap(Map<?, ?> map) throws Exception {
        StringBuilder mapJson = new StringBuilder("{");
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            String key = handleValue(entry.getKey());
            String value = handleValue(entry.getValue());
            mapJson.append(key).append(":").append(value);
            if (iterator.hasNext()) {
                mapJson.append(",");
            }
        }

        mapJson.append("}");
        return mapJson.toString();
    }

    public void addTypeAdapter(Class<?> clazz, JsonSerializer<?> serializer) {
        typeAdapters.put(clazz, serializer);
    }
}
