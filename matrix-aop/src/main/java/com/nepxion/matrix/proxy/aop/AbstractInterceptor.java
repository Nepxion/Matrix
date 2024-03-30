package com.nepxion.matrix.proxy.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.nepxion.matrix.proxy.constant.ProxyConstant;
import com.nepxion.matrix.proxy.exception.ProxyException;
import com.nepxion.matrix.proxy.util.ProxyUtil;

public abstract class AbstractInterceptor implements MethodInterceptor {
    // 通过标准反射来获取变量名，适用于接口代理
    // 只作用在Java8下，同时需要在IDE和Maven里设置"-parameters"的Compiler Argument。参考如下：
    // 1)Eclipse加"-parameters"参数：https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument
    // 2)Idea加"-parameters"参数：http://blog.csdn.net/royal_lr/article/details/52279993
    private ParameterNameDiscoverer standardReflectionParameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();

    // Spring Boot 3.2已移除LocalVariableTableParameterNameDiscoverer
    // 通过解析字节码文件的本地变量表来获取的，只支持CGLIG(ASM library)，适用于类代理
    // private ParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public boolean isCglibAopProxy(MethodInvocation invocation) {
        return getProxyClassName(invocation).contains(ProxyConstant.CGLIB);
    }

    public String getProxyType(MethodInvocation invocation) {
        boolean isCglibAopProxy = isCglibAopProxy(invocation);
        if (isCglibAopProxy) {
            return ProxyConstant.PROXY_TYPE_CGLIB;
        } else {
            return ProxyConstant.PROXY_TYPE_REFLECTIVE;
        }
    }

    public Class<?> getProxyClass(MethodInvocation invocation) {
        return invocation.getClass();
    }

    public String getProxyClassName(MethodInvocation invocation) {
        return getProxyClass(invocation).getCanonicalName();
    }

    public Object getProxiedObject(MethodInvocation invocation) {
        return invocation.getThis();
    }

    public Class<?> getProxiedClass(MethodInvocation invocation) {
        return getProxiedObject(invocation).getClass();
    }

    public String getProxiedClassName(MethodInvocation invocation) {
        return getProxiedClass(invocation).getCanonicalName();
    }

    public Class<?>[] getProxiedInterfaces(MethodInvocation invocation) {
        return getProxiedClass(invocation).getInterfaces();
    }

    public Annotation[] getProxiedClassAnnotations(MethodInvocation invocation) {
        return getProxiedClass(invocation).getAnnotations();
    }

    public Method getMethod(MethodInvocation invocation) {
        return invocation.getMethod();
    }

    public String getMethodName(MethodInvocation invocation) {
        return getMethod(invocation).getName();
    }

    public Annotation[][] getMethodParameterAnnotations(MethodInvocation invocation) {
        return getMethod(invocation).getParameterAnnotations();
    }

    public Class<?>[] getMethodParameterTypes(MethodInvocation invocation) {
        return getMethod(invocation).getParameterTypes();
    }

    public String getMethodParameterTypesValue(MethodInvocation invocation) {
        Class<?>[] parameterTypes = getMethodParameterTypes(invocation);
        String parameterTypesValue = ProxyUtil.toString(parameterTypes);

        return parameterTypesValue;
    }

    // 获取变量名
    public String[] getMethodParameterNames(MethodInvocation invocation) {
        Method method = getMethod(invocation);

        boolean isCglibAopProxy = isCglibAopProxy(invocation);
        if (isCglibAopProxy) {
            // Spring Boot 3.2已移除LocalVariableTableParameterNameDiscoverer
            // return localVariableTableParameterNameDiscoverer.getParameterNames(method);
            return standardReflectionParameterNameDiscoverer.getParameterNames(method);
        } else {
            return standardReflectionParameterNameDiscoverer.getParameterNames(method);
        }
    }

    public Annotation[] getMethodAnnotations(MethodInvocation invocation) {
        return getMethod(invocation).getAnnotations();
    }

    public Object[] getArguments(MethodInvocation invocation) {
        return invocation.getArguments();
    }

    // 获取参数注解对应的参数值。例如方法doXX(@MyAnnotation String id)，根据MyAnnotation注解和String类型，获得id的值
    // 但下面的方法只适用于同时满足如下三个条件的场景（更多场景请自行扩展）：
    // 1. 方法注解parameterAnnotationType，只能放在若干个参数中的一个
    // 2. 方法注解parameterAnnotationType，对应的参数类型必须匹配给定的类型parameterType
    // 3. 方法注解parameterAnnotationType，对应的参数值不能为null
    @SuppressWarnings("unchecked")
    public <T> T getValueByParameterAnnotation(MethodInvocation invocation, Class<?> parameterAnnotationType, Class<T> parameterType) {
        String methodName = getMethodName(invocation);
        String parameterTypesValue = getMethodParameterTypesValue(invocation);
        Annotation[][] parameterAnnotations = getMethodParameterAnnotations(invocation);
        Object[] arguments = getArguments(invocation);

        if (ArrayUtils.isEmpty(parameterAnnotations)) {
            throw new ProxyException("Not found any annotations");
        }

        T value = null;
        int annotationIndex = 0;
        int valueIndex = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType() == parameterAnnotationType) {
                    // 方法注解在方法上只允许有一个（通过判断value的重复赋值）
                    if (value != null) {
                        throw new ProxyException("Only 1 annotation=" + parameterAnnotationType.getName() + " can be added in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "]");
                    }

                    Object object = arguments[valueIndex];
                    // 方法注解的值不允许为空
                    if (object == null) {
                        throw new ProxyException("Value for annotation=" + parameterAnnotationType.getName() + " in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "] is null");
                    }

                    // 方法注解的类型不匹配
                    if (object.getClass() != parameterType) {
                        throw new ProxyException("Type for annotation=" + parameterAnnotationType.getName() + " in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "] must be " + parameterType.getName());
                    }

                    value = (T) object;

                    annotationIndex++;
                }
            }
            valueIndex++;
        }

        if (annotationIndex == 0) {
            return null;
            // throw new MatrixException("Not found annotation=" + parameterAnnotationType.getName() + " in method [name=" + methodName + ", parameterTypes=" + parameterTypesValue + "]");
        }

        return value;
    }

    public String getSpelKey(MethodInvocation invocation, String key) {
        String[] parameterNames = getMethodParameterNames(invocation);
        Object[] arguments = getArguments(invocation);

        // 使用SPEL进行Key的解析
        ExpressionParser parser = new SpelExpressionParser();

        // SPEL上下文
        EvaluationContext context = new StandardEvaluationContext();

        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }

        return parser.parseExpression(key).getValue(context, String.class);
    }
}