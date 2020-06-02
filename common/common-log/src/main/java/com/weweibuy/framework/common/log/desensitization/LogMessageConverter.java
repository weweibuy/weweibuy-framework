package com.weweibuy.framework.common.log.desensitization;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.weweibuy.framework.common.log.context.RequestLogContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author durenhao
 * @date 2019/9/13 16:42
 **/
public class LogMessageConverter extends ClassicConverter {

    private static final ServiceLoader<PatternReplaceConfig> LOADER = ServiceLoader.load(PatternReplaceConfig.class);

    private static final Map<String, PatternReplace> FILED_PATTERN_MAP = new HashMap<>();

    private static final Map<String, PatternReplace> NO_MATCH_FILED_PATTERN_MAP = new ConcurrentHashMap<>();

    private static final String NO_MATCH_PATTERN_TEMPLATE = "(%s)(\":\").*?(?=(\"))";

    private static final String NO_MATCH_REPLACE = "$1$2$3******";

    static {
        init();
        initCustom();
    }


    @Override
    public String convert(ILoggingEvent event) {
        RequestLogContext requestContext = RequestLogContext.getRequestContext();
        if (requestContext == null || !Level.INFO.getName().equals(event.getLevel().levelStr) ||
                !requestContext.getLogSensitization() || CollectionUtils.isEmpty(requestContext.getSensitizationFieldSet())) {
            return event.getFormattedMessage();
        }
        String message = event.getFormattedMessage();
        Set<String> sensitizationFieldSet = requestContext.getSensitizationFieldSet();

        for (String filed : sensitizationFieldSet) {
            PatternReplace patternReplace = FILED_PATTERN_MAP.get(filed);
            if (patternReplace == null) {
                patternReplace = NO_MATCH_FILED_PATTERN_MAP.computeIfAbsent(filed, key ->
                        new PatternReplace(Pattern.compile(String.format(NO_MATCH_PATTERN_TEMPLATE, key)), NO_MATCH_REPLACE));
            }
            message = patternReplace.replace(message);
        }

        return message;
    }


    private static void init() {
        Pattern phonePattern = Pattern.compile("(mobile|phone|phoneNo)(\":\")(\\d{3})\\d{4}(\\d{4})(\")");
        PatternReplace phonePatternReplace = new PatternReplace(phonePattern, "$1$2$3****$4$5");
        FILED_PATTERN_MAP.put("mobile", phonePatternReplace);
        FILED_PATTERN_MAP.put("phone", phonePatternReplace);
        FILED_PATTERN_MAP.put("phoneNo", phonePatternReplace);

        Pattern certIdPattern = Pattern.compile("(certId|idCard)(\":\")(\\d{6})\\d{8,11}(\\w{1})(\")");
        PatternReplace certIdPatternReplace = new PatternReplace(certIdPattern, "$1$2$3**************$4$5");
        FILED_PATTERN_MAP.put("certId", certIdPatternReplace);
        FILED_PATTERN_MAP.put("idCard", certIdPatternReplace);

        Pattern pwd = Pattern.compile("(password|pwd|appSecret)(\":\").*?(?=(\"))");
        PatternReplace pwdPatternReplace = new PatternReplace(pwd, "$1$2$3******");

        FILED_PATTERN_MAP.put("pwd", pwdPatternReplace);
        FILED_PATTERN_MAP.put("password", pwdPatternReplace);
        FILED_PATTERN_MAP.put("appSecret", pwdPatternReplace);

        Pattern namePattern = Pattern.compile("(fullName)(\":\")([\\u4e00-\\u9fa5]{1})([\\u4e00-\\u9fa5]*)(\")");
        PatternReplace namePatternReplace = new PatternReplace(namePattern, "$1$2$3**$5");
        FILED_PATTERN_MAP.put("fullName", namePatternReplace);

        Pattern addressPattern = Pattern.compile("(address)(\":\")([\\u4e00-\\u9fa5]{4})([\\u4e00-\\u9fa5]*)(\")");
        PatternReplace addressPatternReplace = new PatternReplace(addressPattern, "$1$2$3**$5");
        FILED_PATTERN_MAP.put("address", addressPatternReplace);
    }


    public static void initCustom() {
        for (PatternReplaceConfig config : LOADER) {
            config.addPatternReplace(FILED_PATTERN_MAP);
        }
    }

    @Data
    @AllArgsConstructor
    public static class PatternReplace {

        private Pattern pattern;

        private String replace;

        private String replace(String msg) {
            Matcher matcher = pattern.matcher(msg);
            if (matcher.find()) {
                return matcher.replaceAll(replace);
            }
            return msg;
        }

    }


}
