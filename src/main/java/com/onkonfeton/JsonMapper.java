package com.onkonfeton;

import com.onkonfeton.adapter.JsonDeserializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMapper {

    private Map<Class<?>, JsonDeserializer<?>> typeAdapters = new HashMap<>();

    public <T> T assignValues(Map<String, Object> map, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();

                if (map.containsKey(fieldName)) {
                    field.setAccessible(true);

                    Object value = map.get(fieldName);

                    if (typeAdapters.containsKey(field.getType())) {
                        JsonDeserializer<?> typeAdapter = typeAdapters.get(field.getType());
                        value = typeAdapter.deserialize(value);
                        field.set(instance, value);
                    }else if (field.getType().equals(String.class)
                                || Number.class.isAssignableFrom(field.getType())
                                || field.getType().isPrimitive()) {

                        field.set(instance, value);

                    } else if (List.class.isAssignableFrom(field.getType())) {
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Class<?> listType = (Class<?>) pt.getActualTypeArguments()[0];

                            List<?> list = convertToList((List<?>) value, listType);
                            field.set(instance, list);
                        }
                    } else if (Map.class.isAssignableFrom(value.getClass())) {
                        Object nestedObject = assignValues((Map<String, Object>) value, field.getType());
                        field.set(instance, nestedObject);
                    } else {
                        throw new RuntimeException("No serializers found for type " + field.getType());
                    }
                }
            }

            return instance;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> convertToList(List<?> sourceList, Class<T> targetType) {
        List<T> resultList = new ArrayList<>();
        for (Object item : sourceList) {
            if (item instanceof Map) {
                resultList.add(assignValues((Map<String, Object>) item, targetType));
            } else {
                resultList.add((T) item);
            }
        }
        return resultList;
    }

    public void addTypeAdapter(Class<?> classOf, JsonDeserializer<?> jsonDeserializer) {
        typeAdapters.put(classOf, jsonDeserializer);
    }
}
