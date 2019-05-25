
package com.leyton.repository.inter;

import java.util.Optional;

public interface CacheRepository {

    boolean set(String key, String value, String setOperation, String timeUnits, long time);

    Optional<String> get(String key);

    boolean del(String key);
}
