
package com.leyton.repository.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.leyton.repository.inter.CacheRepository;
import com.netflix.dyno.jedis.DynoJedisClient;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CacheRepositoryImp implements CacheRepository {

    private static final String OK_STATUS = "OK";

    private DynoJedisClient dynoJedisClient;

    @Override
    public boolean set(String key, String value, String setOperation, String timeUnits, long time) {
        boolean status = false;
        String result = dynoJedisClient.set(key, value, setOperation, timeUnits, time);
        if (Objects.nonNull(result))
            status = result.equals(OK_STATUS);
        return status;
    }

    @Override
    public Optional<String> findByKey(String key) {
        String result = dynoJedisClient.get(key);
        return Objects.nonNull(result) ? Optional.of(result) : Optional.empty();
    }

    @Override
    public boolean deleteByKey(String key) {
        return dynoJedisClient.del(key) > 0;
    }
}
