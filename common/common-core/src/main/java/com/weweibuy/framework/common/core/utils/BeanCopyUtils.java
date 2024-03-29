package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * bean 拷贝工具
 *
 * @author durenhao
 * @date 2020/3/13 21:05
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCopyUtils {

    private static final Map<Key, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>(64);

    /**
     * 拷贝bean 对象
     *
     * @param src       源对象
     * @param destClazz 必须有默认空参构造
     * @param <T>
     * @return
     */
    public static <T> T copy(Object src, Class<T> destClazz) {
        if (src == null || destClazz == null) {
            return null;
        }
        Class<?> srcClazz = src.getClass();
        BeanCopier beanCopier = beanCopier(srcClazz, destClazz);
        T newInstance = newInstance(destClazz);
        beanCopier.copy(src, newInstance, null);
        return newInstance;
    }


    /**
     * 拷贝bean 对象
     *
     * @param src        源对象
     * @param destObject 必须有默认空参构造
     * @param <T>
     * @return
     */
    public static <T> T copy(Object src, T destObject) {
        if (destObject == null) {
            return null;
        }
        if (src == null) {
            return destObject;
        }
        BeanCopier beanCopier = beanCopier(src.getClass(), destObject.getClass());
        beanCopier.copy(src, destObject, null);
        return destObject;
    }


    /**
     * 集合拷贝
     *
     * @param collection
     * @param srcClazz
     * @param destClazz
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> copyCollection(Collection<T> collection, Class<T> srcClazz, Class<R> destClazz) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }
        if (srcClazz == null || destClazz == null) {
            throw new NullPointerException();
        }
        BeanCopier beanCopier = beanCopier(srcClazz, destClazz);
        return collection.stream()
                .map(c -> {
                    R newInstance = newInstance(destClazz);
                    beanCopier.copy(c, newInstance, null);
                    return newInstance;
                })
                .collect(Collectors.toList());

    }

    /**
     * 获取 BeanCopier
     *
     * @param srcClazz
     * @param destClazz
     * @return
     */
    private static BeanCopier beanCopier(Class srcClazz, Class destClazz) {
        Key key = new Key(srcClazz, destClazz);
        return BEAN_COPIER_MAP.computeIfAbsent(key, k -> BeanCopier.create(srcClazz, destClazz, false));
    }

    private static <T> T newInstance(Class<T> destClazz) {
        try {
            return destClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(destClazz.getName() + " 必须含有公共空参构造");
        }
    }


    @AllArgsConstructor
    @EqualsAndHashCode
    static class Key implements Comparable<Key> {

        private Class srcClazz;

        private Class destClazz;

        @Override
        public int compareTo(Key other) {
            int result = this.srcClazz.toString().compareTo(other.srcClazz.toString());
            if (result == 0 && this.destClazz != null) {
                if (other.destClazz == null) {
                    return 1;
                }
                result = this.destClazz.toString().compareTo(other.destClazz.toString());
            }
            return result;
        }
    }

}
