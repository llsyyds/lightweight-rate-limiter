package io.llsfish.ratelimiter.parser.script;

import io.llsfish.spi.SPI;

/**
 * @author Redick01
 */
@SPI
public interface ScriptParser {

    /**
     * get expression value.
     * @param expressKey expression key
     * @param arguments arguments
     * @return expression value
     */
    String getExpressionValue(String expressKey, Object[] arguments);

    /**
     * is lua script.
     * @param script script
     * @return is lua script
     */
    boolean isScript(String script);
}
