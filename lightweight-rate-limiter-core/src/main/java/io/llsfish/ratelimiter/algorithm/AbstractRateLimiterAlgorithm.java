package io.llsfish.ratelimiter.algorithm;

import io.llsfish.ratelimiter.common.constant.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Redick01
 */
public abstract class AbstractRateLimiterAlgorithm implements RateLimiterAlgorithm<List<Long>> {

    private final String scriptName;

    private final String script;

    protected AbstractRateLimiterAlgorithm(final String scriptName) {
        this.scriptName = scriptName;
        String scriptPath = Constant.SCRIPT_PATH + scriptName;
        /**
         * 使用 getclass().getClassLoader.getResourceAsStream(path) 时，路径应始终从类路径的根开始，不需要以 / 开头。
         *
         * 使用 getclass().getResourceAsStream(path) 时，如果路径以 / 开头，则从类路径的根目录开始。如果不以 / 开头，则它会被解析为相对于该类的包路径。
         */
        try (InputStream is = getClass().getResourceAsStream(scriptPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            this.script = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading script from path: " + scriptPath, e);
        }
    }

    /**
     * Gets key name.
     *
     * @return the key name
     */
    protected abstract String getKeyName();

    @Override
    public String getScriptName() {
        return scriptName;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public List<String> getKeys(final String id) {
        String prefix = getKeyName() + ".{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }
}
