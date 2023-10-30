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
 * @date 2021/5/17 22:14
 **/
public class CustomBasePackageSelectors {


    public static Predicate<RequestHandler> basePackage(Set<String> basePackage) {
        return input -> declaringClass(input)
                .map(handlerPackage(basePackage))
                .orElse(true) && hasSwaggerAnnotation(input);
    }

    private static Function<Class<?>, Boolean> handlerPackage(Collection<String> basePackage) {
        return input ->
                basePackage.stream()
                        .anyMatch(ClassUtils.getPackageName(input)::startsWith);
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }

    private static boolean hasSwaggerAnnotation(RequestHandler input) {
        boolean present = input.findControllerAnnotation(Api.class).isPresent();
        return present && input.isAnnotatedWith(ApiOperation.class);
    }

}
