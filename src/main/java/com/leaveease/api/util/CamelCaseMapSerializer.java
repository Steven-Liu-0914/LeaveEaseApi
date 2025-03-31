package com.leaveease.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CamelCaseMapSerializer extends JsonSerializer<Map<String, Object>> {

    @Override
    public void serialize(Map<String, Object> originalMap, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Map<String, Object> camelCaseMap = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String originalKey = entry.getKey();
            String camelKey = Character.toLowerCase(originalKey.charAt(0)) + originalKey.substring(1);
            camelCaseMap.put(camelKey, entry.getValue());
        }

        gen.writeObject(camelCaseMap);
    }
}
