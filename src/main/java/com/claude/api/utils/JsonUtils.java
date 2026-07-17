package com.claude.api.utils;

import com.claude.api.exceptions.FrameworkException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Thin wrapper around Jackson's {@link ObjectMapper} for ad-hoc
 * serialization needs outside of Rest Assured's own (de)serialization
 * (e.g., converting fixtures loaded by {@link DataReader} into request
 * POJOs).
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    /**
     * Serializes an object to its JSON string representation.
     *
     * @param value the object to serialize
     * @return the JSON string
     */
    public static String toJson(final Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception ex) {
            throw new FrameworkException("Failed to serialize object to JSON", ex);
        }
    }

    /**
     * Deserializes a JSON string into the given type.
     *
     * @param json the JSON string
     * @param type the target type
     * @param <T>  the target type parameter
     * @return the deserialized instance
     */
    public static <T> T fromJson(final String json, final Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception ex) {
            throw new FrameworkException("Failed to deserialize JSON to " + type.getSimpleName(), ex);
        }
    }

    /**
     * @return the shared {@link ObjectMapper} instance, for callers that need
     * more control (e.g., {@link com.fasterxml.jackson.databind.type.CollectionType}).
     */
    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
