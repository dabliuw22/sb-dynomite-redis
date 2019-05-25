
package com.leyton.service.inter;

import java.util.Optional;

public interface CacheService {

    <T> boolean set(String key, T value, String timeUnits, long time);

    <T> Optional<T> get(String key, Class<T> clazz);

    boolean del(String key);
}
