package com.nepxion.matrix.registrar.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.aop.framework.ProxyFactory;

public class MyProxy {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaze) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addInterface(interfaze);
        proxyFactory.addAdvice(new MyInterceptor());
        proxyFactory.setOptimize(false);

        return (T) proxyFactory.getProxy(interfaze.getClassLoader());
    }
}