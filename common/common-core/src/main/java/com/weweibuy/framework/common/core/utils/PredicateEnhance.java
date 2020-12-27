package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 断言工具
 *
 * @author durenhao
 * @date 2020/12/26 11:58
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateEnhance<T> {

    private static final PredicateEnhance<?> PREDICATE_FALSE = new PredicateEnhance<>(new Object(), o -> false);

    private static final PredicateEnhance<?> PREDICATE_TRUE = new PredicateEnhance<>(new Object(), o -> true);

    private final T value;

    private final Predicate<T> predicate;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder<T> {

        private T value;

        private Predicate<T> predicate;

        public PredicateEnhance<T> withPredicate(Predicate<T> predicate) {
            this.predicate = predicate;
            return new PredicateEnhance(value, predicate);
        }

    }

    public static <T> PredicateEnhance.Builder<T> of(T value) {
        Builder<T> builder = new Builder<>();
        builder.value = value;
        return builder;
    }

    public static <T> T predicateThenGet(boolean b, Supplier<T> trueSupplier, Supplier<T> falseSupplier) {
        if (b) {
            return trueSupplier.get();
        }
        return falseSupplier.get();
    }

    public static void predicateAndAction(boolean b, Runnable trueAction, Runnable falseAction) {
        if (b) {
            trueAction.run();
        } else {
            falseAction.run();
        }
    }

    public static <T> PredicateEnhance<T> falsePredicate() {
        return (PredicateEnhance<T>) PREDICATE_FALSE;
    }

    public static <T> PredicateEnhance<T> truePredicate() {
        return (PredicateEnhance<T>) PREDICATE_TRUE;
    }

    public void trueThenAction(Runnable trueAction) {
        if (predicate()) {
            trueAction.run();
        }
    }

    public void trueThenConsumer(Consumer<T> consumer) {
        if (predicate()) {
            consumer.accept(value);
        }
    }

    public void falseThenAction(Runnable trueAction) {
        if (!predicate()) {
            trueAction.run();
        }
    }

    public void falseThenConsumer(Consumer<T> consumer) {
        if (!predicate()) {
            consumer.accept(value);
        }
    }

    public <T> T get(Supplier<T> trueSupplier, Supplier<T> falseSupplier) {
        return predicateThenGet(predicate.test(value), trueSupplier, falseSupplier);
    }

    public <R> R map(Function<T, R> trueFunction, Function<T, R> falseFunction) {
        if (predicate()) {
            return trueFunction.apply(value);
        }
        return falseFunction.apply(value);
    }

    public <R> R mapOrGet(Function<T, R> trueFunction, Supplier<R> falseSupplier) {
        if (predicate()) {
            return trueFunction.apply(value);
        }
        return falseSupplier.get();
    }

    public <R> R getOrMap(Supplier<R> trueSupplier, Function<T, R> falseFunction) {
        if (predicate()) {
            return trueSupplier.get();
        }
        return falseFunction.apply(value);
    }

    public boolean predicate() {
        return predicate.test(value);
    }


}
