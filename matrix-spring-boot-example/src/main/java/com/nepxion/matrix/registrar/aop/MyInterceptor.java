package com.nepxion.matrix.registrar.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class MyInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String interfaze = invocation.getMethod().getDeclaringClass().getCanonicalName();
        MyBean myBean = null;
        try {
            myBean = MyContextAware.getBean(interfaze, MyBean.class);
            System.out.println("接口=" + interfaze + "，对应注解对象 =" + myBean);
            return "接口[" + interfaze + "]找到@MyAnnotation，走特殊代理";
        } catch (NoSuchBeanDefinitionException e) { // 如果接口类上没有相关的注解，会抛出NoSuchBeanDefinitionException
            return "接口[" + interfaze + "]没找到@MyAnnotation，走其它代理";
        }
    }
}