# Matrix AOP
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Nepxion Matrix是一款基于Spring AutoProxy机制的AOP框架，具有很高的通用性，健壮性，灵活性和易用性。它统一封装接口(Spring)代理和类代理(CGLIB)，注解无论在接口和类的头部或者方法上，都可以让业务端无编程的被有效切面，可以轻松快速实现对接口或者类的复杂代理业务

## 简介
    1. 实现接口走Spring代理，类走CGLIB代理
    2. 实现同一进程中，可以接口代理和类代理同存
    3. 实现对类或者接口名上注解Annotation，方法上注解Annotation的快速扫描，并开放处理接口供业务端实现
    4. 实现“只扫描不代理”，“既扫描又代理”；代理支持“只代理类或者接口名上注解”、“只代理方法上的注解”、“全部代理”三种模式；扫描支持“只扫描类或者接口名上注解”、“只扫描方法上的注解”、“全部扫描”三种模式
    5. 实现“代理和扫描多个注解“
    6. 实现“支持多个切面实现类Interceptor做调用拦截”  
    7. 实现“自身调用自身的注解方法，达到切面效果”，提供自动装配(Spring 4.3.X以上的版本支持)和AopContext.currentProxy()两种方式
    8. 实现“只扫描指定目录”和“扫描全局目录”两种方式
    9. 实现根据Java8的特性来获取注解对应方法上的变量名(不是变量类型)，支持标准反射和字节码CGLIG(ASM library)来获取，前者适用于接口代理，后者适用于类代理
       标准反射的方式，需要在IDE和Maven里设置"-parameters"的Compiler Argument。参考如下：
       1)Eclipse加"-parameters"参数：https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument
       2)Idea加"-parameters"参数：http://blog.csdn.net/royal_lr/article/details/52279993

## 应用
Matrix框架一般可以应用到如下场景中：

    1. 对于有复杂AOP使用场景的，用Matrix可以简化你的切面开发。例如：
       1.1 根据不同的业务逻辑，指定所有的注解由同一个或者多个拦截类来拦截；也可以指定不同的注解由不同的切面拦截类来拦截；更可以指定不同的接口和实现类，由不同的拦截类来拦截
       1.2 如果注解很多，可以指定，你只关心哪些类注解，哪些方法注解，不管这些注解是你自定义的，还是系统定义的
    2. 注解加在接口上，还是实现类上，或者没有接口的类，可以随意换
    3. 扫描到一个注解后，你可以做一些处理，例如你可以把注解对应的数据存入数据库
    4. 强大的注解扫描和拦截功能，在不侵入业务代码的前提下(只是需要在业务端加入一个注解而已)，你可以实现业务应用，例如API监控统计、API健康检查等

## 注意
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

## 示例
示例1，通过全局拦截器实现对类头部注解的扫描和代理，详细用法可参考示例3
```java
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

    // 多个包路径，用“;”分隔
    private static final String SCAN_PACKAGES = "com.nepxion.matrix.simple";

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

示例2，通过额外拦截器实现对方法头部注解的扫描和代理，详细用法可参考示例3
```java
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
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.mode.ProxyMode;
import com.nepxion.matrix.mode.ScanMode;
import com.nepxion.matrix.simple.service.MyService2Impl;

// 通过额外拦截器实现对方法头部注解的扫描和代理
@Component("myAutoScanProxyForMethod")
public class MyAutoScanProxyForMethod extends DefaultAutoScanProxy {
    private static final long serialVersionUID = -481395242918857264L;

    private static final String[] SCAN_PACKAGES = { "com.nepxion.matrix.simple" };

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

示例3，该示例比较复杂，为了演示Auto proxy强大的功能
```java
package com.nepxion.matrix.complex.aop;

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
import com.nepxion.matrix.complex.service.MyService3;
import com.nepxion.matrix.complex.service.MyService4Impl;
import com.nepxion.matrix.mode.ProxyMode;
import com.nepxion.matrix.mode.ScanMode;

@Component("myAutoScanProxy")
public class MyAutoScanProxy extends AbstractAutoScanProxy {
    private static final long serialVersionUID = -481395242918857264L;

    private static final String[] SCAN_PACKAGES = { "com.nepxion.matrix.complex" };

    @SuppressWarnings("rawtypes")
    private Class[] commonInterceptorClasses;

    @SuppressWarnings("rawtypes")
    private Class[] classAnnotations;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    @Autowired
    private MyInterceptor3 myInterceptor3;

    private Object[] myInterceptor3Array;

    @Autowired
    private MyInterceptor4 myInterceptor4;

    private Object[] myInterceptor4Array;

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
            commonInterceptorClasses = new Class[] { MyInterceptor3.class, MyInterceptor4.class };
        }
        // return commonInterceptorClasses;
        return null;
    }

    @Override
    protected Object[] getAdditionalInterceptors(Class<?> targetClass) {
        // 返回额外的拦截类实例列表，拦截类必须实现MethodInterceptor接口，分别对不同的接口或者类赋予不同的拦截类，可以多个
        // 如果返回null，额外切面代理关闭

        // 由int值来表示使用策略
        int strategy = 3;

        if (strategy == 1) {
            // 使用策略1：根据接口或者类决定选择哪个切面代理
            // 例如下面示例中，如果所代理的接口是MyService3，执行myInterceptor3切面拦截
            if (targetClass == MyService3.class) {
                return getMyInterceptor3Array();
            // 例如下面示例中，如果所代理的类是MyService4Impl，执行myInterceptor4切面拦截
            } else if (targetClass == MyService4Impl.class) {
                return getMyInterceptor4Array();
            }
        } else if (strategy == 2) {
            // 使用策略2：根据接口或者类头部的注解决定选择哪个切面代理
            // 例如下面示例中，如果所代理的接口或者类头部“只要”出现MyAnnotation3，所有方法都执行myInterceptor3切面拦截
            MyAnnotation3 myAnnotation3 = AnnotationUtils.findAnnotation(targetClass, MyAnnotation3.class);
            if (myAnnotation3 != null) {
                return getMyInterceptor3Array();
            }
        } else if (strategy == 3) {
            // 使用策略3：根据接口或者类的方法注解决定选择哪个切面代理
            // 例如下面示例中，如果所代理的接口或者类的方法中“只要”出现MyAnnotation5，所有方法都执行myInterceptor3切面拦截；“只要”出现MyAnnotation6，所有方法都执行myInterceptor4切面拦截
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method method : methods) {
                MyAnnotation5 myAnnotation5 = AnnotationUtils.findAnnotation(method, MyAnnotation5.class);
                if (myAnnotation5 != null) {
                    return getMyInterceptor3Array();
                }
                MyAnnotation6 myAnnotation6 = AnnotationUtils.findAnnotation(method, MyAnnotation6.class);
                if (myAnnotation6 != null) {
                    return getMyInterceptor4Array();
                }
            }
        }

        return null;
    }

    private Object[] getMyInterceptor3Array() {
        if (myInterceptor3Array == null) {
            // Lazyloader模式，避免重复构造Class数组
            myInterceptor3Array = new Object[] { myInterceptor3 };
        }
        return myInterceptor3Array;
    }

    private Object[] getMyInterceptor4Array() {
        if (myInterceptor4Array == null) {
            // Lazyloader模式，避免重复构造Class数组
            myInterceptor4Array = new Object[] { myInterceptor4 };
        }
        return myInterceptor4Array;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getClassAnnotations() {
        // 返回接口名或者类名上的注解列表，可以多个, 如果接口名或者类名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
        // 如果返回null，则对列表中的注解不做代理和扫描
        // 例如下面示例中，一旦你的接口或者类名出现MyAnnotation3或者MyAnnotation4，则所在的接口或者类将被代理和扫描
        if (classAnnotations == null) {
            // Lazyloader模式，避免重复构造Class数组
            classAnnotations = new Class[] { MyAnnotation3.class, MyAnnotation4.class };
        }
        return classAnnotations;
        // return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        // 返回接口或者类的方法名上的注解，可以多个，如果接口或者类中方法名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
        // 如果返回null，则对列表中的注解不做代理和扫描
        // 例如下面示例中，一旦你的方法名上出现MyAnnotation5或者MyAnnotation6，则该方法所在的接口或者类将被代理和扫描
        if (methodAnnotations == null) {
            // Lazyloader模式，避免重复构造Class数组
            methodAnnotations = new Class[] { MyAnnotation5.class, MyAnnotation6.class };
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

代理结果
```java
2017-05-01 12:33:40.683 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:185] - --------------- Matrix Proxy Information ---------------
2017-05-01 12:33:40.684 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:188] - Class [com.nepxion.matrix.complex.service.MyService3] is proxied by common interceptors [com.nepxion.matrix.complex.aop.MyInterceptor3,com.nepxion.matrix.complex.aop.MyInterceptor4], proxyTargetClass=false
2017-05-01 12:33:40.684 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:192] - Class [com.nepxion.matrix.complex.service.MyService3] is proxied by additional interceptors [[com.nepxion.matrix.complex.aop.MyInterceptor3@26549e60]], proxyTargetClass=false
2017-05-01 12:33:40.684 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:194] - -------------------------------------------------
2017-05-01 12:33:40.692 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:185] - --------------- Matrix Proxy Information ---------------
2017-05-01 12:33:40.692 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:188] - Class [com.nepxion.matrix.complex.service.MyService4Impl] is proxied by common interceptors [com.nepxion.matrix.complex.aop.MyInterceptor3,com.nepxion.matrix.complex.aop.MyInterceptor4], proxyTargetClass=true
2017-05-01 12:33:40.692 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:192] - Class [com.nepxion.matrix.complex.service.MyService4Impl] is proxied by additional interceptors [[com.nepxion.matrix.complex.aop.MyInterceptor3@26549e60]], proxyTargetClass=true
2017-05-01 12:33:40.692 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:194] - -------------------------------------------------
2017-05-01 12:33:40.714 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:185] - --------------- Matrix Proxy Information ---------------
2017-05-01 12:33:40.714 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:188] - Class [com.nepxion.matrix.complex.service.MyService5Impl] is proxied by common interceptors [com.nepxion.matrix.complex.aop.MyInterceptor3,com.nepxion.matrix.complex.aop.MyInterceptor4], proxyTargetClass=true
2017-05-01 12:33:40.714 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:192] - Class [com.nepxion.matrix.complex.service.MyService5Impl] is proxied by additional interceptors [[com.nepxion.matrix.complex.aop.MyInterceptor3@26549e60]], proxyTargetClass=true
2017-05-01 12:33:40.714 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:194] - -------------------------------------------------
2017-05-01 12:33:40.725 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:185] - --------------- Matrix Proxy Information ---------------
2017-05-01 12:33:40.725 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:188] - Class [com.nepxion.matrix.complex.service.MyService6Impl] is proxied by common interceptors [com.nepxion.matrix.complex.aop.MyInterceptor3,com.nepxion.matrix.complex.aop.MyInterceptor4], proxyTargetClass=true
2017-05-01 12:33:40.725 INFO [main][com.nepxion.matrix.aop.AbstractAutoScanProxy:194] - -------------------------------------------------
``` 

切面结果
```java
My Interceptor 3 :
   proxyClassName=org.springframework.aop.framework.ReflectiveMethodInvocation
   className=com.nepxion.matrix.complex.service.MyService3Impl
   classAnnotations=
      @org.springframework.stereotype.Service(value=myService3Impl)
   interfaceName=com.nepxion.matrix.complex.service.MyService3
   interfaceAnnotations=
      @com.nepxion.matrix.complex.aop.MyAnnotation3(description=MyAnnotation3, name=MyAnnotation3, label=MyAnnotation3)
      @com.nepxion.matrix.complex.aop.MyAnnotation4(description=MyAnnotation4, name=MyAnnotation4, label=MyAnnotation4)
   methodName=doE
   methodAnnotations=
      @com.nepxion.matrix.complex.aop.MyAnnotation5(description=MyAnnotation5, name=MyAnnotation5, label=MyAnnotation5)
   parameterAnnotation[MyAnnotation7]'s value=E
   arguments=
      E
   parameterNames=
      id
``` 