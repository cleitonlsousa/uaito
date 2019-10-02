package com.uaito.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


public class MessageSerialize extends StdSerializer {

    public MessageSerialize() {
        super(Messages.class);
    }

    public MessageSerialize(Class t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {

        Messages errors = (Messages) value;

        generator.writeStartObject();
        generator.writeFieldName("message");
        generator.writeString(errors.getMessage());
        generator.writeEndObject();
    }
}
