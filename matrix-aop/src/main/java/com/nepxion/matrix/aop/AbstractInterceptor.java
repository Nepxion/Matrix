package com.nepxion.matrix.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class AbstractInterceptor implements MethodInterceptor {
    private static final String CGLIB = "Cglib";

    // 通过标准反射来获取变量名，适用于接口代理
    // 只作用在Java8下，同时需要在IDE和Maven里设置"-parameters"的Compiler Argument。参考如下：
    // https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument
    private ParameterNameDiscoverer standardReflectionParameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();

    // 通过解析字节码文件的本地变量表来获取的，只支持CGLIG(ASM library)，适用于类代理
    private ParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    // 获取变量名
    public String[] getParameterNames(MethodInvocation invocation) {
        Object proxiedObject = invocation.getThis();
        Class<?> proxiedClass = proxiedObject.getClass();
        String proxiedClassName = proxiedClass.getCanonicalName();

        Method method = invocation.getMethod();

        boolean isCglibAopProxy = proxiedClassName.contains(CGLIB);
        if (isCglibAopProxy) {
            return localVariableTableParameterNameDiscoverer.getParameterNames(method);
        } else {
            return standardReflectionParameterNameDiscoverer.getParameterNames(method);
        }
    }

    public String getSpelKey(MethodInvocation invocation, String key) {
        String[] parameterNames = getParameterNames(invocation);
        Object[] arguments = invocation.getArguments();

        // 使用SPEL进行key的解析
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