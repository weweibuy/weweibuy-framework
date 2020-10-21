package com.weweibuy.framerwork.statemachine.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author zhang.suxing
 * @date 2020/10/21 22:00
 **/
public class Predicteutils<T> {

    public Predicate toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Class clazz, Class eneity) {
        //通过子类获取父类的通用类型
        Type superclass = this.getClass();
        //父类的通用类型转化为参数化类型
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        //获得泛型的类型
        Type modelType = parameterizedType.getActualTypeArguments()[0];

        Class type = (Class) modelType;
        Class<?> javaType = root.getModel().getJavaType();
        // 获取数据库实体类
        //获取root对象
        //传入特殊
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            field.setAccessible(true);


        }
        return cb.and(cb.equal(root.get(""), ""));
    }
}
