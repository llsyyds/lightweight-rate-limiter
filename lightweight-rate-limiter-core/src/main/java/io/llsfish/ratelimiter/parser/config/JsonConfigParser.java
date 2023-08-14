package io.llsfish.ratelimiter.parser.config;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.llsfish.ratelimiter.common.constant.Constant;
import io.llsfish.spi.Join;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Redick01
 */
@Join
@SuppressWarnings("unchecked")
public class JsonConfigParser implements ConfigParser {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
    }

    @Override
    public Map<Object, Object> doParse(String content) throws IOException {
        return doParse(content, Constant.CONFIG_PREFIX);
    }

    @Override
    public Map<Object, Object> doParse(String content, String prefix) throws JsonProcessingException {
        Map<String, Object> originMap = MAPPER.readValue(content, LinkedHashMap.class);
        Map<Object, Object> result = Maps.newHashMap();

        flatMap(result, originMap, prefix);
        return result;
    }

    /**
     * config format
     * @param result result
     * @param dataMap dataMap
     * @param prefix prefix
     */
    private void flatMap(Map<Object, Object> result, Map<String, Object> dataMap, String prefix) {

        if (MapUtil.isEmpty(dataMap)) {
            return;
        }

        dataMap.forEach((k, v) -> {
            String fullKey = genFullKey(prefix, k);
            if (v instanceof Map) {
                flatMap(result, (Map<String, Object>) v, fullKey);
            } else if (v instanceof Collection) {
                int count = 0;
                for (Object obj : (Collection<Object>) v) {
                    String kk = "[" + count++ + "]";
                    flatMap(result, Collections.singletonMap(kk, obj), fullKey);
                }
            } else {
                result.put(fullKey, v);
            }
        });
    }

    /**
     * get config key.
     * @param prefix prefix
     * @param key key
     * @return config key
     */
    private String genFullKey(String prefix, String key) {
        if (StringUtils.isBlank(prefix)) {
            return key;
        }

        return key.startsWith("[") ? prefix.concat(key) : prefix.concat(".").concat(key);
    }
}
