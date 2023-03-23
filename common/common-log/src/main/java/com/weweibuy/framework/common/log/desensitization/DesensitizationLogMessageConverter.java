package com.weweibuy.framework.common.log.desensitization;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.weweibuy.framework.common.log.mvc.MvcPathMappingOperator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏日志转化器
 *
 * @author durenhao
 * @date 2019/9/13 16:42
 **/
public class DesensitizationLogMessageConverter extends ClassicConverter {

    private static final Integer MAX_MESSAGE_LENGTH = 10000;

    private static final ServiceLoader<PatternReplaceConfig> LOADER = ServiceLoader.load(PatternReplaceConfig.class);

    private static final Map<String, PatternReplace> FILED_PATTERN_MAP = new HashMap<>();

    private static final Map<String, PatternReplace> NO_MATCH_FILED_PATTERN_MAP = new ConcurrentHashMap<>();

    private static final String NO_MATCH_PATTERN_TEMPLATE = "(\"\\s*:\\s*\"|=).*?(?=(\"|,|&))";

    private static final String NO_MATCH_REPLACE = "$1$2******";

    static {
        initDefaultPattern();
        initCustomConfig();
    }


    @Override
    public String convert(ILoggingEvent event) {
        return MvcPathMappingOperator.getSensitizationField()
                .filter(s -> Level.INFO.getName().equals(event.getLevel().levelStr))
                .filter(s -> MvcPathMappingOperator.getSensitizationLogger()
                        .map(l -> event.getLoggerName().equals(l))
                        .orElse(true))
                .map(s -> doDesensitization(event.getFormattedMessage(), s))
                .orElse(event.getFormattedMessage());
    }


    private String doDesensitization(String message, Set<String> sensitizationFieldSet) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            message = message.substring(0, MAX_MESSAGE_LENGTH);
        }
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

    private static void initDefaultPattern() {
        /*
         * 正则中 () 中匹配为一组
         * $1 代表从前到后中第一个匹配的组 , $2 为第二个; 不在()内的不计入组
         */
        Pattern phonePattern = Pattern.compile("(mobile|phone|phoneNo)(\"\\s*:\\s*\"|=)(\\d{3})\\d{4}(\\d{4})");
        PatternReplace phonePatternReplace = new PatternReplace(phonePattern, "$1$2$3****$4");
        FILED_PATTERN_MAP.put("mobile", phonePatternReplace);
        FILED_PATTERN_MAP.put("phone", phonePatternReplace);
        FILED_PATTERN_MAP.put("phoneNo", phonePatternReplace);

        Pattern certIdPattern = Pattern.compile("(certId|idCard|idNo)(\"\\s*:\\s*\"|=)(\\d{6})\\d{8,11}(\\w{1})");
        PatternReplace certIdPatternReplace = new PatternReplace(certIdPattern, "$1$2$3**************$4");
        FILED_PATTERN_MAP.put("certId", certIdPatternReplace);
        FILED_PATTERN_MAP.put("idCard", certIdPatternReplace);
        FILED_PATTERN_MAP.put("idNo", certIdPatternReplace);


        Pattern pwd = Pattern.compile("(password|pwd|appSecret)(\"\\s*:\\s*\"|=).*?(?=(\"|,|&))");
        PatternReplace pwdPatternReplace = new PatternReplace(pwd, "$1$2******");

        FILED_PATTERN_MAP.put("pwd", pwdPatternReplace);
        FILED_PATTERN_MAP.put("password", pwdPatternReplace);
        FILED_PATTERN_MAP.put("appSecret", pwdPatternReplace);

        Pattern namePattern = Pattern.compile("(fullName)(\"\\s*:\\s*\"|=)([\\u4e00-\\u9fa5]{1})([\\u4e00-\\u9fa5]*)");
        PatternReplace namePatternReplace = new PatternReplace(namePattern, "$1$2$3**");
        FILED_PATTERN_MAP.put("fullName", namePatternReplace);

        Pattern addressPattern = Pattern.compile("(address)(\"\\s*:\\s*\"|=)([\\u4e00-\\u9fa5]{4}).*?(?=(\"|,|&))");
        PatternReplace addressPatternReplace = new PatternReplace(addressPattern, "$1$2$3***");
        FILED_PATTERN_MAP.put("address", addressPatternReplace);
    }


    private static void initCustomConfig() {
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
            return matcher.replaceAll(replace);
        }

    }


}
