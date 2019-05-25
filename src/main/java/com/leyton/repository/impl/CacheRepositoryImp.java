
package com.leyton.repository.impl;

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
        return dynoJedisClient.set(key, value, setOperation, timeUnits, time).equals(OK_STATUS);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.of(dynoJedisClient.get(key));
    }

    @Override
    public boolean del(String key) {
        return dynoJedisClient.del(key) > 0;
    }
}
