package com.weweibuy.framework.common.swagger.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author durenhao
 * @date 2023/10/30 14:53
 **/
public class AnnotationSelectors {

    public static Predicate<RequestHandler> hasSwaggerAnnotation() {
        return input -> hasSwaggerAnnotation(input);
    }

    private static boolean hasSwaggerAnnotation(RequestHandler input) {
        return input.findControllerAnnotation(Api.class).isPresent()
                && input.isAnnotatedWith(ApiOperation.class);
    }


}
