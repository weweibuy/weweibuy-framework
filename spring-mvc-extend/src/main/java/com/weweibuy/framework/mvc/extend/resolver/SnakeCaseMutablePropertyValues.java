package com.weweibuy.framework.mvc.extend.resolver;

import com.google.common.base.CaseFormat;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author durenhao
 * @date 2020/2/17 22:39
 **/
public class SnakeCaseMutablePropertyValues extends MutablePropertyValues {

    /**
     * Default prefix separator.
     */
    public static final String DEFAULT_PREFIX_SEPARATOR = "_";


    public SnakeCaseMutablePropertyValues(ServletRequest request) {
        this(request, null, null);
    }

    public SnakeCaseMutablePropertyValues(ServletRequest request, @Nullable String prefix) {
        this(request, prefix, DEFAULT_PREFIX_SEPARATOR);
    }

    public SnakeCaseMutablePropertyValues(
            ServletRequest request, @Nullable String prefix, @Nullable String prefixSeparator) {

        super(getParametersStartingWith(
                request, (prefix != null ? prefix + prefixSeparator : null)));
    }

    private static Map<String, Object> getParametersStartingWith(ServletRequest request, @Nullable String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                unprefixed = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, unprefixed);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

}
