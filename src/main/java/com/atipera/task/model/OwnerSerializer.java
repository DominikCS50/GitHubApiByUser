package com.atipera.task.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class OwnerSerializer extends JsonSerializer<Repository.Owner> {
    @Override
    public void serialize(Repository.Owner owner, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(owner.getLogin());
    }
}