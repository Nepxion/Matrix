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

import com.nepxion.matrix.registrar.AbstractAnnotationsRegistrar;

public class MyAnnotationsRegistrar extends AbstractAnnotationsRegistrar {
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
        return MyBean.class;
    }
}