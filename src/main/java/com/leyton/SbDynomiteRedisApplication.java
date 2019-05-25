
package com.leyton;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leyton.service.inter.CacheService;
import com.leyton.util.DynomiteConstant;

@SpringBootApplication
public class SbDynomiteRedisApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SbDynomiteRedisApplication.class);

    @Autowired
    private CacheService cacheService;

    public static void main(String[] args) {
        SpringApplication.run(SbDynomiteRedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String key = "key";
        String newValue = new Date().toString();
        boolean savedValue = cacheService.set(key, newValue,
                DynomiteConstant.ExpireTimeUnits.MILLISECONDS, 30000);
        LOGGER.info("saved-value: [{}]", savedValue);
        String value = cacheService.get(key, String.class).orElse("EMPTY");
        LOGGER.info("value: [{}]", value);
    }
}
