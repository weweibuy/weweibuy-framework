package com.weweibuy.framework.common.swagger.support;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;

import java.util.Set;

/**
 * @author durenhao
 * @date 2021/5/17 22:14
 **/
public class CustomBasePackageSelectors {


    public static Predicate<RequestHandler> basePackage(Set<String> basePackage) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                return declaringClass(input).transform(handlerPackage(basePackage)).or(true);
            }
        };
    }

    private static Function<Class<?>, Boolean> handlerPackage(Set<String> basePackage) {
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
        return Optional.fromNullable(input.declaringClass());
    }

}
