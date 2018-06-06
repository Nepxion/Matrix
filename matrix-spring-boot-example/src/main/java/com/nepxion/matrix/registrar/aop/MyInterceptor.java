package com.nepxion.matrix.registrar.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.MutablePropertyValues;

import com.nepxion.matrix.registrar.AbstractRegistrarInterceptor;

public class MyInterceptor extends AbstractRegistrarInterceptor {
    public MyInterceptor(MutablePropertyValues annotationValues) {
        super(annotationValues);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String interfaze = getInterface(invocation);

        System.out.println("Interface=" + interfaze + ", annotation:name=" + annotationValues.get("name") + ", label=" + annotationValues.get("label") + ", description=" + annotationValues.get("description"));

        // 实现业务代码

        return null;
    }
}