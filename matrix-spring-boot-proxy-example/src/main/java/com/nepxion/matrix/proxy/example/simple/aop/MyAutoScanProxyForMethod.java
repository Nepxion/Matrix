package com.nepxion.matrix.proxy.example.simple.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.proxy.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.proxy.example.simple.service.MyService2Impl;
import com.nepxion.matrix.proxy.mode.ProxyMode;
import com.nepxion.matrix.proxy.mode.ScanMode;

// 通过额外拦截器实现对方法头部注解的扫描和代理
// 该类描述的逻辑是，目标接口或者类的某个方法上如果出现了MyAnnotation2注解，那么该接口或者类下面所有的方法都会被执行扫描和代理，并为该接口或者类指定一个具体的代理类为MyInterceptor2
@Component("myAutoScanProxyForMethod")
public class MyAutoScanProxyForMethod extends DefaultAutoScanProxy {
    private static final long serialVersionUID = -481395242918857264L;

    private static final String[] SCAN_PACKAGES = { "com.nepxion.matrix.proxy.example.simple" };

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    @Autowired
    private MyInterceptor2 myInterceptor2;

    private MethodInterceptor[] myInterceptor2Array;

    public MyAutoScanProxyForMethod() {
        super(SCAN_PACKAGES, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected MethodInterceptor[] getAdditionalInterceptors(Class<?> targetClass) {
        if (targetClass == MyService2Impl.class) {
            return getMyInterceptor2Array();
        }
        return null;
    }

    private MethodInterceptor[] getMyInterceptor2Array() {
        if (myInterceptor2Array == null) {
            myInterceptor2Array = new MethodInterceptor[] { myInterceptor2 };
        }
        return myInterceptor2Array;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        if (methodAnnotations == null) {
            methodAnnotations = new Class[] { MyAnnotation2.class };
        }
        return methodAnnotations;
    }

    @Override
    protected void methodAnnotationScanned(Class<?> targetClass, Method method, Class<? extends Annotation> methodAnnotation) {
        System.out.println("Method annotation scanned, targetClass=" + targetClass + ", method=" + method + ", methodAnnotation=" + methodAnnotation);
    }
}