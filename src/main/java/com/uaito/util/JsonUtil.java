package com.uaito.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uaito.exception.TournamentDetailsParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    @Autowired
    ObjectMapper mapper;

    public Object parse(String json, Class c) throws TournamentDetailsParseException {

        try {

            if(json == null)
                return c.getDeclaredConstructor(c).newInstance();

            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(json, c);

        } catch (Exception e) {

           throw new TournamentDetailsParseException(e.getMessage());

        }

    }

    public String convert(Object object) throws TournamentDetailsParseException {

        try {

            if(object == null)
                return null;

            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.writeValueAsString(object);

        } catch (Exception e) {

            throw new TournamentDetailsParseException(e.getMessage());

        }

    }
}
