package com.nepxion.matrix.util;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;

import com.nepxion.matrix.exception.MatrixException;

public class MatrixUtil {
    // 获取参数注解对应的参数值。例如方法doXX(@MyAnnotation String id)，根据MyAnnotation注解和String类型，获得id的值
    // 但下面的方法只适用于同时满足如下三个条件的场景（更多场景请自行扩展）：
    // 1. 方法注解parameterAnnotationType，只能放在若干个参数中的一个
    // 2. 方法注解parameterAnnotationType，对应的参数类型必须匹配给定的类型parameterType
    // 3. 方法注解parameterAnnotationType，对应的参数值不能为null
    @SuppressWarnings("unchecked")
    public static <T> T getValueByParameterAnnotation(MethodInvocation invocation, Class<?> parameterAnnotationType, Class<T> parameterType) {
        Method method = invocation.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String parameterTypesValue = toString(parameterTypes);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] arguments = invocation.getArguments();

        if (ArrayUtils.isEmpty(parameterAnnotations)) {
            throw new MatrixException("Not found any annotations");
        }

        T value = null;
        int annotationIndex = 0;
        int valueIndex = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType() == parameterAnnotationType) {
                    // 方法注解在方法上只允许有一个（通过判断value的重复赋值）
                    if (value != null) {
                        throw new MatrixException("Only 1 annotation=" + parameterAnnotationType.getName() + " can be added in method [name=" + method.getName() + ", parameterTypes=" + parameterTypesValue + "]");
                    }

                    Object object = arguments[valueIndex];
                    // 方法注解的值不允许为空
                    if (object == null) {
                        throw new MatrixException("Value for annotation=" + parameterAnnotationType.getName() + " in method [name=" + method.getName() + ", parameterTypes=" + parameterTypesValue + "] is null");
                    }

                    // 方法注解的类型不匹配
                    if (object.getClass() != parameterType) {
                        throw new MatrixException("Type for annotation=" + parameterAnnotationType.getName() + " in method [name=" + method.getName() + ", parameterTypes=" + parameterTypesValue + "] must be " + parameterType.getName());
                    }

                    value = (T) object;

                    annotationIndex++;
                }
            }
            valueIndex++;
        }

        if (annotationIndex == 0) {
            throw new MatrixException("Not found annotation=" + parameterAnnotationType.getName() + " in method [name=" + method.getName() + ", parameterTypes=" + parameterTypesValue + "]");
        }

        return value;
    }

    // 转换Class数组成字符串格式
    public static String toString(Class<?>[] parameterTypes) {
        if (ArrayUtils.isEmpty(parameterTypes)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            builder.append("," + clazz.getCanonicalName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }
}