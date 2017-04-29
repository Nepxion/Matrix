# Matrix AOP

基于Spring AOP AutoProxy机制定制，可以轻松快速实现对接口或者类的复杂代理业务

## 介绍
1. 实现接口走Spring代理，类走CGLIB代理
2. 实现同一进程中，可以接口代理和类代理同存
3. 实现对类或者接口名上注解Annotation，方法上注解Annotation的快速扫描，并开放处理接口供业务端实现
4. 实现“只扫描不代理”，“既扫描又代理”；代理支持“只代理类或者接口名上注解”、“只代理方法上的注解”、“全部代理”三种模式；扫描支持“只扫描类或者接口名上注解”、“只扫描方法上的注解”、“全部扫描”三种模式
5. 实现“代理和扫描多个注解“
6. 实现“支持多个切面实现类Interceptor做调用拦截”  
7. 实现“自身调用自身的注解方法，达到切面效果”，提供自动装配和AopContext.currentProxy()两种方式
8. 实现“只扫描指定目录”和“扫描全局目录”两种方式

## 使用
具体参考com.nepxion.matrix.test下的示例。该示例比较复杂，为了演示Auto proxy强大的功能。你在使用的时候，考虑到实际使用场景，会比较简单
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

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.AbstractAutoScanProxy;
import com.nepxion.matrix.mode.ProxyMode;
import com.nepxion.matrix.mode.ScanMode;
import com.nepxion.matrix.test.service.MyService1;
import com.nepxion.matrix.test.service.MyService2Impl;

@Component("myAutoScanProxy")
public class MyAutoScanProxy extends AbstractAutoScanProxy {
    private static final long serialVersionUID = -481395242918857264L;

    private static final String[] SCAN_PACKAGES = { "com.nepxion.matrix" };

    @SuppressWarnings("rawtypes")
    private Class[] commonInterceptorClasses;

    @SuppressWarnings("rawtypes")
    private Class[] classAnnotations;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    @Autowired
    private MyInterceptor1 myInterceptor1;

    private Object[] myInterceptor1Array;

    @Autowired
    private MyInterceptor2 myInterceptor2;

    private Object[] myInterceptor2Array;

    // 可以设定多个全局拦截器，也可以设定多个额外拦截器；可以设定拦截触发由全局拦截器执行，还是由额外拦截器执行
    // 如果同时设置了全局和额外的拦截器，那么它们都同时工作，全局拦截器先运行，额外拦截器后运行
    public MyAutoScanProxy() {
        // SCAN_PACKAGES                           扫描目录，如果不指定，则扫描全局。两种方式运行结果没区别，只是指定扫描目录加快扫描速度，同时可以减少缓存量
        // ProxyMode.BY_CLASS_OR_METHOD_ANNOTATION 对全部注解都进行代理
        // ProxyMode.BY_CLASS_ANNOTATION_ONLY      只代理类或者接口名上注解
        // ProxyMode.BY_METHOD_ANNOTATION_ONLY     只代理方法上的注解
        // ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION 对全部注解都进行扫描
        // ScanMode.FOR_CLASS_ANNOTATION_ONLY      只扫描类或者接口名上注解
        // ScanMode.FOR_METHOD_ANNOTATION_ONLY     只扫描方法上的注解
        super(SCAN_PACKAGES, ProxyMode.BY_CLASS_OR_METHOD_ANNOTATION, ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION);
    }

    @Override
    protected Class<? extends MethodInterceptor>[] getCommonInterceptors() {
        // 返回具有调用拦截的全局切面实现类，拦截类必须实现MethodInterceptor接口, 可以多个
        // 如果返回null， 全局切面代理关闭
        if (commonInterceptorClasses == null) {
            // Lazyloader模式，避免重复构造Class数组
            commonInterceptorClasses = new Class[] { MyInterceptor1.class, MyInterceptor2.class };
        }
        // return commonInterceptorClasses;

        return null;
    }

    @Override
    protected Object[] getAdditionalInterceptors(Class<?> targetClass) {
        // 返回额外的拦截类实例列表，拦截类必须实现MethodInterceptor接口，分别对不同的接口或者类赋予不同的拦截类，可以多个
        // 如果返回null， 额外切面代理关闭

        // 由int值来表示使用策略
        int strategy = 3;

        if (strategy == 1) {
            // 使用策略1：根据接口或者类决定选择哪个切面代理
            // 例如下面示例中，如果所代理的接口是MyService1，执行myInterceptor1切面拦截
            if (targetClass == MyService1.class) {
                return getMyInterceptor1Array();
                // 例如下面示例中，如果所代理的类是MyService2Impl，执行myInterceptor2切面拦截
            } else if (targetClass == MyService2Impl.class) {
                return getMyInterceptor2Array();
            }
        } else if (strategy == 2) {
            // 使用策略2：根据接口或者类头部的注解决定选择哪个切面代理
            // 例如下面示例中，如果所代理的接口或者类头部“只要”出现MyAnnotation1，所有方法都执行myInterceptor1切面拦截
            MyAnnotation1 myAnnotation1 = AnnotationUtils.findAnnotation(targetClass, MyAnnotation1.class);
            if (myAnnotation1 != null) {
                return getMyInterceptor1Array();
            }
        } else if (strategy == 3) {
            // 使用策略3：根据接口或者类的方法注解决定选择哪个切面代理
            // 例如下面示例中，如果所代理的接口或者类的方法中“只要”出现MyAnnotation3，所有方法都执行myInterceptor1切面拦截；“只要”出现MyAnnotation4，所有方法都执行myInterceptor2切面拦截
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method method : methods) {
                MyAnnotation3 myAnnotation3 = AnnotationUtils.findAnnotation(method, MyAnnotation3.class);
                if (myAnnotation3 != null) {
                    return getMyInterceptor1Array();
                }
                MyAnnotation4 myAnnotation4 = AnnotationUtils.findAnnotation(method, MyAnnotation4.class);
                if (myAnnotation4 != null) {
                    return getMyInterceptor2Array();
                }
            }
        }

        return null;
    }

    private Object[] getMyInterceptor1Array() {
        if (myInterceptor1Array == null) {
            // Lazyloader模式，避免重复构造Class数组
            myInterceptor1Array = new Object[] { myInterceptor1 };
        }
        return myInterceptor1Array;
    }

    private Object[] getMyInterceptor2Array() {
        if (myInterceptor2Array == null) {
            // Lazyloader模式，避免重复构造Class数组
            myInterceptor2Array = new Object[] { myInterceptor2 };
        }
        return myInterceptor2Array;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getClassAnnotations() {
        // 返回接口名或者类名上的注解列表，可以多个, 如果接口名或者类名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
        // 如果返回null，则对列表中的注解不做代理和扫描
        // 例如下面示例中，一旦你的接口或者类名出现MyAnnotation1或者MyAnnotation2，则所在的接口或者类将被代理和扫描
        if (classAnnotations == null) {
            // Lazyloader模式，避免重复构造Class数组
            classAnnotations = new Class[] { MyAnnotation1.class, MyAnnotation2.class };
        }
        return classAnnotations;

        // return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        // 返回接口或者类的方法名上的注解，可以多个，如果接口或者类中方法名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
        // 如果返回null，则对列表中的注解不做代理和扫描
        // 例如下面示例中，一旦你的方法名上出现MyAnnotation3或者MyAnnotation4，则该方法所在的接口或者类将被代理和扫描
        if (methodAnnotations == null) {
            // Lazyloader模式，避免重复构造Class数组
            methodAnnotations = new Class[] { MyAnnotation3.class, MyAnnotation4.class };
        }
        return methodAnnotations;
        // return null;
    }

    @Override
    protected void classAnnotationScanned(Class<?> targetClass, Class<? extends Annotation> classAnnotation) {
        // 一旦指定的接口或者类名上的注解被扫描到，将会触发该方法
        System.out.println("Class annotation scanned, targetClass=" + targetClass + ", classAnnotation=" + classAnnotation);
    }

    @Override
    protected void methodAnnotationScanned(Class<?> targetClass, Method method, Class<? extends Annotation> methodAnnotation) {
        // 一旦指定的接口或者类的方法名上的注解被扫描到，将会触发该方法
        System.out.println("Method annotation scanned, targetClass=" + targetClass + ", method=" + method + ", methodAnnotation=" + methodAnnotation);
    }
}
``` 
在切面实现类中，你可以拿到一切你想要的东西
```java
My Interceptor 1 :
   proxyClassName=org.springframework.aop.framework.ReflectiveMethodInvocation
   className=com.nepxion.matrix.test.service.MyService1Impl
   classAnnotations=
      @org.springframework.stereotype.Service(value=myService1Impl)
   interfaceName=com.nepxion.matrix.test.service.MyService1
   interfaceAnnotations=
      @com.nepxion.matrix.test.aop.MyAnnotation1(description=MyAnnotation1, name=MyAnnotation1, label=MyAnnotation1)
      @com.nepxion.matrix.test.aop.MyAnnotation2(description=MyAnnotation2, name=MyAnnotation2, label=MyAnnotation2)
   methodName=doA
   methodAnnotations=
      @com.nepxion.matrix.test.aop.MyAnnotation3(description=MyAnnotation3, name=MyAnnotation3, label=MyAnnotation3)

My Interceptor 2 :
   proxyClassName=org.springframework.aop.framework.CglibAopProxy.CglibMethodInvocation
   className=com.nepxion.matrix.test.service.MyService2Impl
   classAnnotations=
      @com.nepxion.matrix.test.aop.MyAnnotation1(description=MyAnnotation1, name=MyAnnotation1, label=MyAnnotation1)
      @org.springframework.stereotype.Service(value=myService2Impl)
      @com.nepxion.matrix.test.aop.MyAnnotation2(description=MyAnnotation2, name=MyAnnotation2, label=MyAnnotation2)
   methodName=doC
   methodAnnotations=
      @com.nepxion.matrix.test.aop.MyAnnotation3(description=MyAnnotation3, name=MyAnnotation3, label=MyAnnotation3)
``` 