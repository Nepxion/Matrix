package com.nepxion.matrix.aop;

/**
 * <p>Title: Nepxion Matrix AOP</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.mode.ProxyMode;
import com.nepxion.matrix.mode.ScanMode;
import com.nepxion.matrix.util.AnnotationUtils;

public abstract class AbstractAutoScanProxy extends AbstractAutoProxyCreator {
    private static final long serialVersionUID = -364406999854610869L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAutoScanProxy.class);

    // Bean名称和Bean对象关联
    private final Map<String, Object> beanMap = new HashMap<String, Object>();

    // Spring容器中哪些接口或类需要被代理
    private final Map<String, Boolean> proxyMap = new HashMap<String, Boolean>();

    // Spring容器中哪些类是类代理, 哪些类是通过它的接口做代理
    private final Map<String, Boolean> proxyTargetClassMap = new HashMap<String, Boolean>();

    // 通过注解确定代理
    private ProxyMode proxyMode;

    // 扫描注解后的处理
    private ScanMode scanMode;

    public AbstractAutoScanProxy() {
        this(ProxyMode.BY_CLASS_OR_METHOD_ANNOTATION, ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION);
    }

    public AbstractAutoScanProxy(ProxyMode proxyMode, ScanMode scanMode) {
        this.proxyMode = proxyMode;
        this.scanMode = scanMode;

        LOG.info("Proxy mode is {}", proxyMode);
        LOG.info("Scan mode is {}", scanMode);

        // 可以设定多个通用代理器, 也可以设定多个额外代理器；可以设定被代理类由通用代理器执行, 还是由额外代理器执行
        // 设置通用(Common) Advisor代理器（实现于MethodInterceptor接口）
        Class<?>[] interceptorClasses = getInterceptorClasses();
        if (ArrayUtils.isNotEmpty(interceptorClasses)) {
            String[] interceptorNames = new String[interceptorClasses.length];
            for (int i = 0; i < interceptorClasses.length; i++) {
                Class<?> interceptorClass = interceptorClasses[i];
                interceptorNames[i] = interceptorClass.getAnnotation(Component.class).value();
            }
            setInterceptorNames(interceptorNames);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 前置扫描, 把Bean名称和Bean对象关联存入Map
        Object value = super.postProcessBeforeInitialization(bean, beanName);

        beanMap.put(beanName, bean);

        return value;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
        // 根据Bean名称获取Bean对象
        Object bean = beanMap.get(beanName);

        // 获取最终目标类
        Class<?> targetClass = null;
        if (bean != null /* && AopUtils.isCglibProxy(bean) */) {
            targetClass = AopProxyUtils.ultimateTargetClass(bean);
        } else {
            targetClass = beanClass;
        }

        // Spring容器扫描实现类
        if (!targetClass.isInterface()) {
            // 扫描接口（从实现类找到它的所有接口）
            if (targetClass.getInterfaces() != null) {
                for (Class<?> targetInterface : targetClass.getInterfaces()) {
                    Object[] result = scanAndProxyForTarget(targetInterface, beanName, false);
                    if (result != null) {
                        return result;
                    }
                }
            }

            // 扫描实现类（如果接口上没找到注解, 就找实现类的注解）
            Object[] result = scanAndProxyForTarget(targetClass, beanName, true);
            if (result != null) {
                return result;
            }
        }

        // 如果满足注解扫描的条件, 返回PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS,
        // 否则返回DO_NOT_PROXY
        // 1. PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS（=new Object[0]）：
        //    proxy without additional interceptors, just the common ones, 只从设置了从interceptorNames解析处理的列表中获取
        // 2. DO_NOT_PROXY（=null）：
        //    do not proxy, 不执行代理
        return DO_NOT_PROXY;
    }

    @Override
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        Boolean proxyTargetClass = proxyTargetClassMap.get(beanName);
        LOG.info("Bean class [{}] is proxied by [{}], proxyTargetClass is {}", beanClass, AnnotationUtils.convertParameter(getInterceptorClasses()), proxyTargetClass);

        if (proxyTargetClass != null) {
            return proxyTargetClass;
        }

        return super.shouldProxyTargetClass(beanClass, beanName);
    }

    protected Object[] scanAndProxyForTarget(Class<?> targetClass, String beanName, boolean isProxyTargetClass) {
        String targetClassName = targetClass.getCanonicalName();
        // 排除java开头的接口, 例如java.io.Serializable, java.io.Closeable等, 执行不被代理
        if (StringUtils.isNotEmpty(targetClassName) && !targetClassName.startsWith("java.")) {
            // 避免对同一个接口或者类扫描多次
            Boolean proxied = proxyMap.get(targetClassName);
            if (proxied != null) {
                if (proxied) {
                    return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                }
            } else {
                Object[] result = null;
                switch (proxyMode) {
                // 只通过扫描到接口名或者类名上的注解后，来确定是否要代理
                    case BY_CLASS_ANNOTATION_ONLY:
                        result = scanAndProxyForClass(targetClass, targetClassName, beanName, isProxyTargetClass);
                        break;
                    // 只通过扫描到接口或者类方法上的注解后，来确定是否要代理
                    case BY_METHOD_ANNOTATION_ONLY:
                        result = scanAndProxyForMethod(targetClass, targetClassName, beanName, isProxyTargetClass);
                        break;
                    // 上述两者都可以
                    case BY_CLASS_OR_METHOD_ANNOTATION:
                        Object[] classProxyResult = scanAndProxyForClass(targetClass, targetClassName, beanName, isProxyTargetClass);
                        // 没有接口或类名上扫描到目标注解，那么扫描接口或类的方法上的目标注解
                        Object[] methodProxyResult = scanAndProxyForMethod(targetClass, targetClassName, beanName, isProxyTargetClass);
                        if (classProxyResult == PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS || methodProxyResult == PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS) {
                            result = PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                        } else {
                            result = DO_NOT_PROXY;
                        }
                        break;
                }

                // 是否需要代理
                proxyMap.put(targetClassName, Boolean.valueOf(result != DO_NOT_PROXY));

                // 是接口代理还是类代理
                if (result != DO_NOT_PROXY) {
                    proxyTargetClassMap.put(beanName, isProxyTargetClass);
                }

                return result;
            }
        }

        return DO_NOT_PROXY;
    }

    protected Object[] scanAndProxyForClass(Class<?> targetClass, String targetClassName, String beanName, boolean isProxyTargetClass) {
        // 判断目标注解是否标注在接口名或者类名上
        boolean proxied = false;
        Class<? extends Annotation>[] classAnnotations = getClassAnnotations();
        if (ArrayUtils.isNotEmpty(classAnnotations)) {
            for (Class<? extends Annotation> classAnnotation : classAnnotations) {
                if (targetClass.isAnnotationPresent(classAnnotation)) {
                    // 是否执行“注解扫描后处理”
                    if (scanMode == ScanMode.FOR_CLASS_ANNOTATION_ONLY || scanMode == ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION) {
                        classAnnotationScanned(targetClass, classAnnotation);
                    } else {
                        // 如果“注解扫描后处理”不开启, 没必要再往下执行循环，直接返回
                        return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                    }

                    // 目标注解被扫描到, proxied赋值为true, 即认为该接口或者类被代理
                    if (!proxied) {
                        proxied = true;
                    }
                }
            }
        }

        return proxied ? PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS : DO_NOT_PROXY;
    }

    protected Object[] scanAndProxyForMethod(Class<?> targetClass, String targetClassName, String beanName, boolean isProxyTargetClass) {
        // 判断目标注解是否标注在方法上
        boolean proxied = false;
        Class<? extends Annotation>[] methodAnnotations = getMethodAnnotations();
        if (ArrayUtils.isNotEmpty(methodAnnotations)) {
            for (Method method : targetClass.getDeclaredMethods()) {
                for (Class<? extends Annotation> methodAnnotation : methodAnnotations) {
                    if (method.isAnnotationPresent(methodAnnotation)) {
                        // 是否执行“注解扫描后处理”
                        if (scanMode == ScanMode.FOR_METHOD_ANNOTATION_ONLY || scanMode == ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION) {
                            methodAnnotationScanned(targetClass, method, methodAnnotation);
                        } else {
                            // 如果“注解扫描后处理”不开启, 没必要再往下执行循环，直接返回
                            return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                        }

                        // 目标注解被扫描到, proxied赋值为true, 即认为该接口或者类被代理
                        if (!proxied) {
                            proxied = true;
                        }
                    }
                }
            }
        }

        return proxied ? PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS : DO_NOT_PROXY;
    }

    // 返回拦截类列表, 拦截类必须实现MethodInterceptor接口
    // 一般只有一个拦截类, 也可以多个拦截类
    protected abstract Class<? extends MethodInterceptor>[] getInterceptorClasses();

    // 返回接口名或者类名上的注解, 如果接口名或者类名上存在该注解, 即认为该接口或者类需要被代理
    // 一般只有一个注解, 也可以多个注解
    protected abstract Class<? extends Annotation>[] getClassAnnotations();

    // 返回接口或者类的方法名上的注解, 如果接口或者类中方法名上存在该注解, 即认为该接口或者类需要被代理
    // 一般只有一个注解, 也可以多个注解
    protected abstract Class<? extends Annotation>[] getMethodAnnotations();

    // 扫描到接口名或者类名上的注解后, 所要做的处理
    protected abstract void classAnnotationScanned(Class<?> targetClass, Class<? extends Annotation> classAnnotation);

    // 扫描到接口或者类的方法名上的注解后, 所要做的处理
    protected abstract void methodAnnotationScanned(Class<?> targetClass, Method method, Class<? extends Annotation> methodAnnotation);
}