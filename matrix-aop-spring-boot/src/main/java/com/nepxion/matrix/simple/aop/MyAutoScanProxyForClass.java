package com.nepxion.matrix.simple.aop;

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

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.mode.ProxyMode;
import com.nepxion.matrix.mode.ScanMode;

// 通过全局拦截器实现对类头部注解的扫描和代理
@Component("myAutoScanProxyForClass")
public class MyAutoScanProxyForClass extends DefaultAutoScanProxy {
    private static final long serialVersionUID = -5968030133395182024L;

    private static final String[] SCAN_PACKAGES = { "com.nepxion.matrix.simple" };

    @SuppressWarnings("rawtypes")
    private Class[] commonInterceptorClasses;

    @SuppressWarnings("rawtypes")
    private Class[] classAnnotations;

    public MyAutoScanProxyForClass() {
        super(SCAN_PACKAGES, ProxyMode.BY_CLASS_ANNOTATION_ONLY, ScanMode.FOR_CLASS_ANNOTATION_ONLY);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends MethodInterceptor>[] getCommonInterceptors() {
        if (commonInterceptorClasses == null) {
            commonInterceptorClasses = new Class[] { MyInterceptor1.class };
        }
        return commonInterceptorClasses;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getClassAnnotations() {
        if (classAnnotations == null) {
            classAnnotations = new Class[] { MyAnnotation1.class };
        }
        return classAnnotations;
    }

    @Override
    protected void classAnnotationScanned(Class<?> targetClass, Class<? extends Annotation> classAnnotation) {
        System.out.println("Class annotation scanned, targetClass=" + targetClass + ", classAnnotation=" + classAnnotation);
    }
}