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

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.AbstractInterceptor;

@Component("myInterceptor2")
public class MyInterceptor2 extends AbstractInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }
}