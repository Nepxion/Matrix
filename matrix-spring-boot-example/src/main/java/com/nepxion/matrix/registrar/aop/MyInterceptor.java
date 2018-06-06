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
        Object interfaze = annotationValues.get("interfaze");
        Object name = annotationValues.get("name");
        Object label = annotationValues.get("label");
        Object description = annotationValues.get("description");
        
        System.out.println("Interface=" + interfaze + ", annotation:name=" + name + ", label=" + label + ", description=" + description);

        // 实现业务代码

        return null;
    }
}