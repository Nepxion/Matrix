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

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.AbstractInterceptor;

@Component("myInterceptor1")
public class MyInterceptor1 extends AbstractInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String proxyClassName = invocation.getClass().getName();
        String methodName = invocation.getMethod().getName();
        Annotation[] annotations = invocation.getMethod().getAnnotations();

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("My Interceptor 1 :");
        System.out.println("   proxyClassName=" + proxyClassName);
        System.out.println("   methodName=" + methodName);
        System.out.println("   annotations=");
        for (Annotation annotation : annotations) {
            System.out.println("               " + annotation.toString());
        }

        return invocation.proceed();
    }
}