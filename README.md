# Nepxion Matrix
[![Total lines](https://tokei.rs/b1/github/Nepxion/Matrix?category=lines)](https://tokei.rs/b1/github/Nepxion/Matrix?category=lines)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/Matrix/blob/master/LICENSE)  [![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/matrix.svg?label=maven%20central)](https://search.maven.org/artifact/com.nepxion/matrix)  [![Javadocs](http://www.javadoc.io/badge/com.nepxion/matrix-aop.svg)](http://www.javadoc.io/doc/com.nepxion/matrix-aop)  [![Build Status](https://travis-ci.org/Nepxion/Matrix.svg?branch=master)](https://travis-ci.org/Nepxion/Matrix)  [![Codacy Badge](https://api.codacy.com/project/badge/Grade/0baa5e8bd30d46e6be7ef936640f64de)](https://www.codacy.com/project/HaojunRen/Matrix/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nepxion/Matrix&amp;utm_campaign=Badge_Grade_Dashboard)  [![Stars](https://img.shields.io/github/stars/Nepxion/Matrix.svg?label=Stars&tyle=flat&logo=GitHub)](https://github.com/Nepxion/Matrix/stargazers)  [![Stars](https://gitee.com/Nepxion/Matrix/badge/star.svg)](https://gitee.com/nepxion/Matrix/stargazers)

Nepxion Matrix是一款集成Spring AutoProxy，Spring Registrar和Spring Import Selector三种机制的AOP框架，具有很高的通用性、健壮性、灵活性和易用性

## 请联系我
微信和公众号

![](http://nepxion.gitee.io/docs/zxing-doc/微信-1.jpg)![](http://nepxion.gitee.io/docs/zxing-doc/公众号-1.jpg)![](http://nepxion.gitee.io/docs/zxing-doc/文档-1.jpg)

## 简介
### Spring AutoProxy机制
它统一封装接口（Spring）代理和类代理（CGLIB），注解无论在接口和类的头部或者方法上，都可以让业务端执行有效切面，可以轻松快速实现对接口或者类的复杂代理业务。代码参考com.nepxion.matrix.proxy，示例参考matrix-spring-boot-proxy-example
- 实现接口走Spring代理，类走CGLIB代理
- 实现通用代理和额外代理两种机制
- 实现同一进程中，可以接口代理和类代理同存
- 实现对类或者接口名上注解Annotation，方法上注解Annotation的快速扫描，并开放处理接口供业务端实现
- 实现“只扫描不代理”，“既扫描又代理”；代理模式ProxyMode，支持“只代理类或者接口名上注解”、“只代理方法上的注解”、“全部代理”三种模式；扫描模式ScanMode，支持“只扫描类或者接口名上注解”、“只扫描方法上的注解”、“全部扫描”三种模式
- 实现“代理和扫描多个注解“
- 实现“支持多个切面实现类Interceptor做调用拦截”  
- 实现“自身调用自身的注解方法，达到切面效果”，提供自动装配（Spring 4.3.X以上的版本支持）和AopContext.currentProxy()两种方式
- 实现“只扫描指定目录”和“扫描全局目录”两种方式
- 实现根据Java8的特性来获取注解对应方法上的变量名（不是变量类型），支持标准反射和字节码CGLIG（ASM library）来获取，前者适用于接口代理，后者适用于类代理

  标准反射的方式，需要在IDE和Maven里设置"-parameters"的Compiler Argument。参考如下：
  - Eclipse加"-parameters"参数：https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument
  - Idea加"-parameters"参数：http://blog.csdn.net/royal_lr/article/details/52279993

### Spring Registrar机制
实现象@FeignClient注解那样，只有接口没有实现类，就能实现注入和动态代理。代码参考com.nepxion.matrix.registrar，示例参考matrix-spring-boot-registrar-example
- 如果本地只有接口并加相关的注解，那么执行对应的切面调用方式
- 如果本地有接口(不管是否加注解)，并也有实现类，那么执行对应的实现类的逻辑

### Spring Import Selector机制
实现象@EnableCircuitBreaker注解那样，入口加上@EnableMyAnnotation，自动初始化对应的Configuration。代码参考com.nepxion.matrix.selector，示例参考matrix-spring-boot-selector-example
- 入口加上@EnableXXX，并提供在spring.factories定义@EnableXXX和Configuration类的关联，达到通过注解的配置与否，控制对应相关上下文对象，例如Bean类的初始化与否
- 提供在application.properties配置参数，达到上述的目的

## 场景
Matrix框架一般可以应用到如下场景中：

### Spring AutoProxy机制
- 对于有复杂AOP使用场景的，用Matrix可以简化你的切面开发。例如：
  - 根据不同的业务逻辑，指定所有的注解由同一个或者多个拦截类来拦截；也可以指定不同的注解由不同的切面拦截类来拦截；更可以指定不同的接口和实现类，由不同的拦截类来拦截
  - 如果注解很多，可以指定，你只关心哪些类注解，哪些方法注解，不管这些注解是你自定义的，还是系统定义的
- 注解加在接口上，还是实现类上，或者没有接口的类，可以随意换
- 扫描到一个注解后，你可以做一些处理，例如你可以把注解对应的数据存入数据库
- 强大的注解扫描和拦截功能，在不侵入业务代码的前提下(只是需要在业务端加入一个注解而已)，你可以实现业务应用，例如API监控统计、API健康检查等

### Spring Registrar机制
> 参考@FeignClient的用法

### Spring Import Selector机制
> 参考@EnableCircuitBreaker的用法

## 兼容
最新版本兼容
- Spring 4.x.x和Spring Boot 1.x.x
- Spring 5.x.x和Spring Boot 2.x.x

## 依赖
```xml
<dependency>
    <groupId>com.nepxion</groupId>
    <artifactId>matrix-aop</artifactId>
    <version>${matrix.version}</version>
</dependency>
```

## Spring AutoProxy机制的示例
### 注意
拦截实现类中@Component注解一定要加名称，因为全局拦截机制是根据名称BeanName来的
```java
@Component("myInterceptor1")
public class MyInterceptor1 extends AbstractInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ......
    }
}
```

### 示例
调用入口1，通过全局拦截器实现对类头部注解的扫描和代理，详细用法可参考示例3
```java
package com.nepxion.matrix.proxy.simple.aop;

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
import org.springframework.stereotype.Component;

import com.nepxion.matrix.proxy.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.proxy.mode.ProxyMode;
import com.nepxion.matrix.proxy.mode.ScanMode;

// 通过全局拦截器实现对类头部注解的扫描和代理
// 该类描述的逻辑是，目标接口或者类头部如果出现了MyAnnotation1注解，那么该接口或者类下面所有的方法都会被执行扫描和代理，代理类为MyInterceptor1
@Component("myAutoScanProxyForClass")
public class MyAutoScanProxyForClass extends DefaultAutoScanProxy {
    private static final long serialVersionUID = -5968030133395182024L;

    // 多个包路径，用“;”分隔
    private static final String SCAN_PACKAGES = "com.nepxion.matrix.proxy.simple";

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
```

调用入口2，通过额外拦截器实现对方法头部注解的扫描和代理
```java
package com.nepxion.matrix.proxy.simple.aop;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.proxy.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.proxy.mode.ProxyMode;
import com.nepxion.matrix.proxy.mode.ScanMode;
import com.nepxion.matrix.proxy.simple.service.MyService2Impl;

// 通过额外拦截器实现对方法头部注解的扫描和代理
// 该类描述的逻辑是，目标接口或者类的某个方法上如果出现了MyAnnotation2注解，那么该接口或者类下面所有的方法都会被执行扫描和代理，并为该接口或者类指定一个具体的代理类为MyInterceptor2
@Component("myAutoScanProxyForMethod")
public class MyAutoScanProxyForMethod extends DefaultAutoScanProxy {
    private static final long serialVersionUID = -481395242918857264L;

    private static final String[] SCAN_PACKAGES = { "com.nepxion.matrix.proxy.simple" };

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    @Autowired
    private MyInterceptor2 myInterceptor2;

    private Object[] myInterceptor2Array;

    public MyAutoScanProxyForMethod() {
        super(SCAN_PACKAGES, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected Object[] getAdditionalInterceptors(Class<?> targetClass) {
        if (targetClass == MyService2Impl.class) {
            return getMyInterceptor2Array();
        }
        return null;
    }

    private Object[] getMyInterceptor2Array() {
        if (myInterceptor2Array == null) {
            myInterceptor2Array = new Object[] { myInterceptor2 };
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
```

更复杂的用法请参考com.nepxion.matrix.proxy.complex目录下的代码

## Spring Registrar机制的示例
> 参考matrix-spring-boot-registrar-example

## Spring Import Selector机制的示例
> 参考matrix-spring-boot-selector-example

## Star走势图
[![Stargazers over time](https://starchart.cc/Nepxion/Matrix.svg)](https://starchart.cc/Nepxion/Matrix)