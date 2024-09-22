package com.onkonfeton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.onkonfeton.domain.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

class JsonLibTest {

    String jsonString = """
            {
                "id": "3b049946-ed7e-40ba-a7cb-f3585409da22",
                "firstName": "Vitya",
                "lastName": "Ak",
                "dateBirth": "2003-11-03",
                "orders": [
                    {
                        "id": "956bb29b-8191-4de5-9e8e-8df759525831",
                        "products": [
                            {
                              "id": "50faf7eb-6792-45a7-a3cd-91bb63de48f6",
                              "name": "Теlеfон",
                              "price": 100.0
                            },
                            {
                              "id": "6b3a9d70-43e0-4c87-b72d-45fe79ee41c4",
                              "name": "Машина",
                              "price": 100.0
                            }
                        ],
                        "createDate": "2023-10-24T17:50:30.5470749+03:00"
                    }
                ]
            }
        """;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeJsonDeserializer())
            .create();

    @Test
    void deserialize() {
        JsonLib jsonLib = new JsonLib();
        Customer customerFromJsonLib = jsonLib.deserialize(jsonString, Customer.class);



        Customer customerFromGson = gson.fromJson(jsonString, Customer.class);

        Assertions.assertEquals(customerFromGson, customerFromJsonLib);
    }

    @Test
    void serialize() {
        JsonLib jsonLib = new JsonLib();
        Customer customer = jsonLib.deserialize(jsonString, Customer.class);

        String jsonFromLib = jsonLib.serialize(customer);

        String jsonFromGson = gson.toJson(customer);

        Assertions.assertEquals(jsonFromGson, jsonFromLib);
    }

    private static class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
    private static class OffsetDateTimeJsonDeserializer implements JsonDeserializer<OffsetDateTime>, JsonSerializer<OffsetDateTime> {
        @Override
        public OffsetDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return OffsetDateTime.parse(jsonElement.getAsString());
        }

        @Override
        public JsonElement serialize(OffsetDateTime offsetDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(offsetDateTime.toString());
        }
    }
}