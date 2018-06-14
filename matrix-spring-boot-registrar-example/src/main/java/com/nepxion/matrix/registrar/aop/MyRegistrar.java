package com.nepxion.matrix.registrar.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.MutablePropertyValues;

import com.nepxion.matrix.registrar.AbstractRegistrar;

public class MyRegistrar extends AbstractRegistrar {
    @Override
    protected Class<? extends Annotation> getEnableAnnotationClass() {
        return EnableMyAnnotation.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return MyAnnotation.class;
    }

    @Override
    protected Class<?> getBeanClass() {
        return MyRegistrarFactoryBean.class;
    }

    @Override
    protected MethodInterceptor getInterceptor(MutablePropertyValues annotationValues) {
        return new MyInterceptor(annotationValues);
    }
}