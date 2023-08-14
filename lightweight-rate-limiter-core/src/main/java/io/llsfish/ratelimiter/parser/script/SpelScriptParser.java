package io.llsfish.ratelimiter.parser.script;

import io.llsfish.ratelimiter.common.enums.Singleton;
import io.llsfish.spi.Join;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Redick01
 */
@Join
public class SpelScriptParser implements ScriptParser {

    /**
     * spel mark.
     */
    private static final String SPEL_FLAG_1 = "#";

    /**
     * spel mark.
     */
    private static final String SPEL_FLAG_2 = "'";

    /**
     * parmameter name.
     */
    private static final String ARGS = "args";

    private static final ConcurrentHashMap<String, Expression> RATE_KEY_CACHE = new ConcurrentHashMap<>();

    @Override
    public String getExpressionValue(String expressKey, Object[] arguments) {
        if (!isScript(expressKey)) {
            return expressKey;
        }
        SpelExpressionParser parser = Singleton.INST.get(SpelExpressionParser.class);
        if (Objects.isNull(parser)) {
            Singleton.INST.single(SpelExpressionParser.class, new SpelExpressionParser());
            parser = Singleton.INST.get(SpelExpressionParser.class);
        }
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        context.setVariable(ARGS, arguments);
        Expression expression = RATE_KEY_CACHE.get(expressKey);
        if (Objects.isNull(expression)) {
            expression = parser.parseExpression(expressKey);
            RATE_KEY_CACHE.put(expressKey, expression);
        }
        return expression.getValue(context, String.class);
    }

    @Override
    public boolean isScript(String script) {
        return script.contains(SPEL_FLAG_1) || script.contains(SPEL_FLAG_2);
    }
}
