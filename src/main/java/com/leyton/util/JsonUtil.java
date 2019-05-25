
package com.leyton.util;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.isNull;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyton.exception.JsonException;

import io.vavr.control.Try;

public class JsonUtil {

    private static final ObjectMapper MAPPER;

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        MAPPER = new ObjectMapper();
        MAPPER.setDateFormat(dateFormat);
    }

    private JsonUtil() {
    }

    public static <T> String toJson(T pojo) {
        return Try.of(() -> MAPPER.writeValueAsString(Match(pojo).of(Case($(isNull()), v -> {
            throw new NullPointerException();
        }), Case($(), v -> v)))).getOrElseThrow(JsonException::new);
    }

    public static <T> T toPojo(String json, Class<T> clazz) {
        return Try.of(() -> MAPPER.readValue(Match(json).of(Case($(isNull()), v -> {
            throw new NullPointerException();
        }), Case($(), v -> v)), clazz)).getOrElseThrow(JsonException::new);
    }
}
