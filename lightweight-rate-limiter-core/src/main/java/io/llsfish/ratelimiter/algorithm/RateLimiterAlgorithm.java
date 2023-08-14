package io.llsfish.ratelimiter.algorithm;

import io.llsfish.spi.SPI;

import java.util.List;

/**
 * @author Redick01
 */
@SPI
public interface RateLimiterAlgorithm<T> {

    /**
     * Gets script name.
     *
     * @return the script name
     */
    String getScriptName();

    /**
     * Gets script.
     *
     * @return the script
     */
    String getScript();

    /**
     * Gets keys.
     *
     * @param id the id
     * @return the keys
     */
    List<String> getKeys(String id);
}
