package io.llsfish.ratelimiter.parser.config;

import com.google.common.collect.Maps;
import io.llsfish.spi.Join;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.Map;

/**
 * @author Redick01
 */
@Join
public class YmlConfigParser implements ConfigParser {

    @Override
    public Map<Object, Object> doParse(String content) throws IOException {
        if (StringUtils.isEmpty(content)) {
            return Maps.newHashMapWithExpectedSize(0);
        }

        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ByteArrayResource(content.getBytes()));
        return bean.getObject();
    }

    @Override
    public Map<Object, Object> doParse(String content, String prefix) {
        throw new UnsupportedOperationException();
    }
}
