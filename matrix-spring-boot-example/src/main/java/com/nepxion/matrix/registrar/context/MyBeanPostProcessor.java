package com.nepxion.matrix.registrar.context;

/**
 * <p>Title: Nepxion EventBus</p>
 * <p>Description: Nepxion EventBus AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 如果是代理类，执行它的相关方法(例如，bean.toString())的时候，实际是去执行被代理接口的方法，所以在BeanPostProcessor使用的时候要注意
        if (bean.getClass().getName().startsWith("com.sun.proxy.$Proxy")) {

        }

        return bean;
    }
}