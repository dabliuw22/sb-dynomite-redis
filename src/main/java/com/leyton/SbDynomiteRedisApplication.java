
package com.leyton;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leyton.repository.inter.CacheRepository;
import com.leyton.util.DynomiteConstant;

@SpringBootApplication
public class SbDynomiteRedisApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SbDynomiteRedisApplication.class);

    @Autowired
    private CacheRepository cacheRepository;

    public static void main(String[] args) {
        SpringApplication.run(SbDynomiteRedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String key = "key";
        String newValue = new Date().toString();
        boolean savedValue =
                cacheRepository.set(key, newValue, DynomiteConstant.SetOperation.EXISTING_KEY,
                        DynomiteConstant.ExpireTimeUnits.MILLISECONDS, 10000);
        LOGGER.info("saved-value: [{}]", savedValue);
        String value = cacheRepository.findByKey(key).orElse("EMPTY");
        LOGGER.info("value: [{}]", value);
    }
}
