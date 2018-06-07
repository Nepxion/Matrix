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
        System.out.println("-------------------------------------------------");
        String interfaze = getInterface(invocation);
        String methodName = getMethodName(invocation);
        Object[] arguments = getArguments(invocation);

        System.out.println("Interface=" + interfaze + ", methodName=" + methodName + ", arguments=" + arguments[0]);

        Class<?> interfaceClass = (Class<?>) annotationValues.get("interfaze");
        String name = annotationValues.get("name").toString();
        String label = annotationValues.get("label").toString();
        String description = annotationValues.get("description").toString();

        System.out.println("Interface class=" + interfaceClass + ", annotation:name=" + name + ", label=" + label + ", description=" + description);
        System.out.println("-------------------------------------------------");

        // 实现业务代码

        return null;
    }
}