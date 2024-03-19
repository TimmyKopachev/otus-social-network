package org.otus.dzmitry.kapachou.highload.cache.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JacksonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public byte[] serialize(String topic, T data) {
        try (ByteArrayOutputStream outputStream = prepareOutputStream(data)) {
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new SerializationException(exception);
        }
    }

    private ByteArrayOutputStream prepareOutputStream(T data) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            objectMapper.writeValue(outputStream, data);
            return outputStream;
        }
    }
}
