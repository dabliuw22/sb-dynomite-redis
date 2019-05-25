
package com.leyton.service.impl;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.Predicates.isNull;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.leyton.exception.InvalidFormatException;
import com.leyton.exception.JsonException;
import com.leyton.repository.inter.CacheRepository;
import com.leyton.service.inter.CacheService;
import com.leyton.util.DynomiteConstant;
import com.leyton.util.JsonUtil;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function5;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CacheServiceImp implements CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImp.class);

    private final CacheRepository cacheRepository;

    @Override
    public <T> boolean set(String key, T value, String timeUnits, long time) {
        Function5<String, String, String, String, Long, Boolean> function = cacheRepository::set;
        return Try.of(() -> function.apply(Match(key).of(Case($(isNull()), v -> {
            throw new InvalidFormatException(new NullPointerException());
        }), Case($(), v -> v)), JsonUtil.toJson(value), DynomiteConstant.SetOperation.EXISTING_KEY,
                timeUnits, time)).onFailure(error -> LOGGER.error("set(UPDATE) error: ", error))
                .recover(error -> Match(error).of(
                        Case($(instanceOf(InvalidFormatException.class)), f -> false),
                        Case($(instanceOf(JsonException.class)), f -> false)))
                .getOrElse(() -> Try
                        .of(() -> function.apply(key, JsonUtil.toJson(value),
                                DynomiteConstant.SetOperation.NON_EXISTENT_KEY, timeUnits, time))
                        .onFailure(error -> LOGGER.error("set(SAVE) error: ", error))
                        .getOrElse(false));
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        Function2<String, Class<T>, Optional<T>> function =
                (k, c) -> Optional.of(JsonUtil.toPojo(cacheRepository.get(k).get(), c));
        return Try.of(() -> function.apply(Match(key).of(Case($(isNull()), v -> {
            throw new InvalidFormatException(new NullPointerException());
        }), Case($(), v -> v)), clazz)).onFailure(error -> LOGGER.error("get() error: ", error))
                .getOrElse(Optional.empty());
    }

    @Override
    public boolean del(String key) {
        Function1<String, Boolean> function = cacheRepository::del;
        return Try.of(() -> function.apply(key))
                .onFailure(error -> LOGGER.error("del() error: ", error)).getOrElseGet(f -> false);
    }
}
