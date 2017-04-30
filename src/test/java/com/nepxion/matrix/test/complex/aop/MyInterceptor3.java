package com.nepxion.matrix.test.complex.aop;

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

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.aop.AbstractInterceptor;
import com.nepxion.matrix.util.MatrixUtils;

@Component("myInterceptor3")
public class MyInterceptor3 extends AbstractInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> proxyClass = invocation.getClass();
        String proxyClassName = proxyClass.getCanonicalName();
        Object proxiedObject = invocation.getThis();
        Object[] arguments = invocation.getArguments();
        Class<?> proxiedClass = proxiedObject.getClass();
        String proxiedClassName = proxiedClass.getCanonicalName();
        Annotation[] classAnnotations = proxiedClass.getAnnotations();

        Method method = invocation.getMethod();
        String methodName = method.getName();
        Annotation[] methodAnnotations = method.getAnnotations();

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("My Interceptor 3 :");
        System.out.println("   proxyClassName=" + proxyClassName);
        System.out.println("   className=" + proxiedClassName);
        System.out.println("   classAnnotations=");
        for (Annotation classAnnotation : classAnnotations) {
            System.out.println("      " + classAnnotation.toString());
        }

        if (proxiedClass.getInterfaces() != null) {
            for (Class<?> proxiedInterface : proxiedClass.getInterfaces()) {
                System.out.println("   interfaceName=" + proxiedInterface.getCanonicalName());
                System.out.println("   interfaceAnnotations=");
                for (Annotation interfaceAnnotation : proxiedInterface.getAnnotations()) {
                    System.out.println("      " + interfaceAnnotation.toString());
                }
            }
        }

        System.out.println("   methodName=" + methodName);
        System.out.println("   methodAnnotations=");
        for (Annotation methodAnnotation : methodAnnotations) {
            System.out.println("      " + methodAnnotation.toString());
        }

        String parameterAnnotationValue = null;
        try {
            parameterAnnotationValue = MatrixUtils.getValueByParameterAnnotation(invocation, MyAnnotation7.class, String.class);
        } catch (Exception e) {
            
        }
        System.out.println("   parameterAnnotation[MyAnnotation7]'s value=" + parameterAnnotationValue);
        
        System.out.println("   arguments=");
        for (int i = 0; i < arguments.length; i++) {
            System.out.println("      " + arguments[i].toString());
        }
        System.out.println("------------------------------------------------------------------------------------------");

        return invocation.proceed();
    }
}