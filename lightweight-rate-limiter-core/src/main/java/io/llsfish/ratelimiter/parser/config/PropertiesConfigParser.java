package io.llsfish.ratelimiter.parser.config;

import io.llsfish.spi.Join;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

/**
 * @author Redick01
 */
@Join
public class PropertiesConfigParser implements ConfigParser {

    @Override
    public Map<Object, Object> doParse(String content) throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(content));
        return properties;
    }

    @Override
    public Map<Object, Object> doParse(String content, String prefix) {
        throw new UnsupportedOperationException();
    }
}
