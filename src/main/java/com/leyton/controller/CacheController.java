
package com.leyton.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leyton.service.inter.CacheService;
import com.leyton.util.DynomiteConstant;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(
        value = {
            "/cache"
        })
@AllArgsConstructor
public class CacheController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

    private CacheService cacheService;

    @GetMapping
    public String getDate(@RequestParam(
            value = "key") String key,
            @RequestParam(
                    value = "time") Long time) {
        return cacheService.get(key, String.class).orElseGet(() -> {
            String value = new Date().toString();
            boolean saved =
                    cacheService.set(key, value, DynomiteConstant.ExpireTimeUnits.SECONDS, time);
            LOGGER.info("SAVED [{}]: {}", value, saved);
            return value;
        });
    }
}
