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

import com.nepxion.matrix.exception.AnnotationException;

public class AnnotationUtils {
    /**
     * 萃取参数注解值
     * 
     * @param invocation
     * @param parameterAnnotationType, 例如UserId
     * @param parameterType, 例如Integer
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueByParameterAnnotation(MethodInvocation invocation, Class<?> parameterAnnotationType, Class<T> parameterType) {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String parameterTypesValue = convertParameter(parameterTypes);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] arguments = invocation.getArguments();

        if (ArrayUtils.isEmpty(parameterAnnotations)) {
            throw new AnnotationException("No annotation=" + parameterAnnotationType.getName() + " in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "] found");
        }

        T value = null;
        int index = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType() == parameterAnnotationType) {
                    // 方法注解在方法上只允许有一个（通过判断value的重复赋值）
                    if (value != null) {
                        throw new AnnotationException("Only 1 annotation=" + parameterAnnotationType.getName() + " can be added in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "]");
                    }

                    Object object = arguments[index];
                    // 方法注解的值不允许为空
                    if (object == null) {
                        throw new AnnotationException("Value for annotation=" + parameterAnnotationType.getName() + " in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "] is null");
                    }

                    // 方法注解的类型不匹配
                    if (object.getClass() != parameterType) {
                        throw new AnnotationException("Type for annotation=" + parameterAnnotationType.getName() + " in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "] must be " + parameterType.getName());
                    }

                    value = (T) object;
                }
            }
            index++;
        }

        return value;
    }

    public static String convertParameter(Class<?>[] parameterTypes) {
        if (ArrayUtils.isEmpty(parameterTypes)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            builder.append("," + clazz.getName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }
}