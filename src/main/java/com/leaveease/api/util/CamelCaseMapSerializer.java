package com.leaveease.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class CamelCaseMapSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // ✅ Only apply custom logic if it's a Map
        if (value instanceof Map<?, ?> map) {
            gen.writeStartObject();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = toCamelCase(entry.getKey().toString());
                gen.writeObjectField(key, entry.getValue());
            }
            gen.writeEndObject();
        } else {
            // ✅ Fallback to Jackson default for DTOs (like DashboardDto)
            gen.writeObject(value);
        }
    }

    private String toCamelCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }
}
