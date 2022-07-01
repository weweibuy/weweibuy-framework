package com.weweibuy.framework.common.swagger.support;

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
        return new Predicate<RequestHandler>() {
            @Override
            public boolean test(RequestHandler input) {
                return declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
            }
        };
    }

    private static Function<Class<?>, Boolean> handlerPackage(Collection<String> basePackage) {
        return new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> input) {
                String packageName = ClassUtils.getPackageName(input);
                return basePackage.stream()
                        .anyMatch(packageName::startsWith);
            }
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }

}
