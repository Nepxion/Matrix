# Matrix AOP

基于Spring AOP AutoProxy机制定制，可以轻松快速实现对接口或者类的复杂代理业务

## 介绍
1. 实现接口走Spring代理，类走CGLIB代理
2. 实现同一进程中，可以接口代理和类代理同存
3. 实现对类或者接口名上注解Annotation，方法上注解Annotation的快速扫描，并开放处理接口供业务端实现
4. 实现“只扫描不代理”，“既扫描又代理”
   4.1 代理支持“只代理类或者接口名上注解”、“只代理方法上的注解”、“全部代理”三种模式
   4.2 扫描支持“只扫描类或者接口名上注解”、“只扫描方法上的注解”、“全部扫描”三种模式
5. 实现“代理和扫描多个注解“
6. 实现“支持多个切面实现类Interceptor做调用拦截”  

## 使用
具体参考com.nepxion.matrix.test下的示例
```java
package com.nepxion.matrix.test.aop;

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

import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.AbstractAutoScanProxy;
import com.nepxion.matrix.aop.AbstractInterceptor;
import com.nepxion.matrix.mode.ProxyMode;
import com.nepxion.matrix.mode.ScanMode;

@Component("myAutoScanProxy")
public class MyAutoScanProxy extends AbstractAutoScanProxy {
    private static final long serialVersionUID = -481395242918857264L;

    public MyAutoScanProxy() {
        // ProxyMode.BY_CLASS_OR_METHOD_ANNOTATION 对全部注解都进行代理
        // ProxyMode.BY_CLASS_ANNOTATION_ONLY      只代理类或者接口名上注解
        // ProxyMode.BY_METHOD_ANNOTATION_ONLY     只代理方法上的注解
        // ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION 对全部注解都进行扫描
        // ScanMode.FOR_CLASS_ANNOTATION_ONLY      只扫描类或者接口名上注解
        // ScanMode.FOR_METHOD_ANNOTATION_ONLY     只扫描方法上的注解
        super(ProxyMode.BY_CLASS_OR_METHOD_ANNOTATION, ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends AbstractInterceptor>[] getInterceptorClasses() {
        // 返回具有调用拦截的切面实现类，可以多个；如果返回null，则不做切面调用
        return new Class[] { MyInterceptor1.class, MyInterceptor2.class };
        // return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getClassAnnotations() {
        // 返回接口或者类名上的注解，可以多个；如果返回null，则对列表中的注解不做代理和扫描
        // 例如下面示例中，一旦你的接口或者类名出现MyAnnotation1或者MyAnnotation2，则所在的接口或者类将被代理和扫描
        return new Class[] { MyAnnotation1.class, MyAnnotation2.class };
        // return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        // 返回方法名上的注解，可以多个；如果返回null，则对列表中的注解不做代理和扫描
        // 例如下面示例中，一旦你的方法名上出现MyAnnotation3或者MyAnnotation4，则该方法所在的接口或者类将被代理和扫描
        return new Class[] { MyAnnotation3.class, MyAnnotation4.class };
        // return null;
    }

    @Override
    protected void classAnnotationScanned(Class<?> targetClass, Class<? extends Annotation> classAnnotation) {
        // 一旦指定的接口或者类名上的注解被扫描到，将会触发classAnnotationScanned方法
        System.out.println("Class annotation scanned, targetClass=" + targetClass + ", classAnnotation=" + classAnnotation);
    }

    @Override
    protected void methodAnnotationScanned(Class<?> targetClass, Method method, Class<? extends Annotation> methodAnnotation) {
        // 一旦指定的方法名上的注解被扫描到，将会触发methodAnnotationScanned方法
        System.out.println("Method annotation scanned, targetClass=" + targetClass + ", method=" + method + ", methodAnnotation=" + methodAnnotation);
    }
}
``` 