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
import com.nepxion.matrix.util.MatrixUtils;

public abstract class AbstractAutoScanProxy extends AbstractAutoProxyCreator {
    private static final long serialVersionUID = 6827218905375993727L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAutoScanProxy.class);

    // Bean名称和Bean对象关联
    private final Map<String, Object> beanMap = new HashMap<String, Object>();

    // Spring容器中哪些接口或类需要被代理
    private final Map<String, Boolean> proxyMap = new HashMap<String, Boolean>();

    // Spring容器中哪些类是类代理，哪些类是通过它的接口做代理
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

        // 可以设定多个全局拦截器，也可以设定多个额外拦截器；可以设定拦截触发由全局拦截器执行，还是由额外拦截器执行
        // 如果同时设置了全局和额外的拦截器，那么它们都同时工作，全局拦截器先运行，额外拦截器后运行
        Class<?>[] commonInterceptorClasses = getCommonInterceptors();
        if (ArrayUtils.isNotEmpty(commonInterceptorClasses)) {
            String[] interceptorNames = new String[commonInterceptorClasses.length];
            for (int i = 0; i < commonInterceptorClasses.length; i++) {
                Class<?> interceptorClass = commonInterceptorClasses[i];
                interceptorNames[i] = interceptorClass.getAnnotation(Component.class).value();
            }
            setInterceptorNames(interceptorNames);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 前置扫描，把Bean名称和Bean对象关联存入Map
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

            // 扫描实现类（如果接口上没找到注解， 就找实现类的注解）
            Object[] result = scanAndProxyForTarget(targetClass, beanName, true);
            if (result != null) {
                return result;
            }
        }

        return DO_NOT_PROXY;
    }

    @Override
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        // 设置不同场景下的接口代理，还是类代理
        Boolean proxyTargetClass = proxyTargetClassMap.get(beanName);
        if (proxyTargetClass != null) {
            return proxyTargetClass;
        }

        return super.shouldProxyTargetClass(beanClass, beanName);
    }

    protected Object[] scanAndProxyForTarget(Class<?> targetClass, String beanName, boolean proxyTargetClass) {
        String targetClassName = targetClass.getCanonicalName();
        Object[] interceptors = getInterceptors(targetClass);
        // 排除java开头的接口，例如java.io.Serializable，java.io.Closeable等，执行不被代理
        if (StringUtils.isNotEmpty(targetClassName) && !targetClassName.startsWith("java.")) {
            // 避免对同一个接口或者类扫描多次
            Boolean proxied = proxyMap.get(targetClassName);
            if (proxied != null) {
                if (proxied) {
                    return interceptors;
                }
            } else {
                Object[] result = null;
                switch (proxyMode) {
                    // 只通过扫描到接口名或者类名上的注解后，来确定是否要代理
                    case BY_CLASS_ANNOTATION_ONLY:
                        result = scanAndProxyForClass(targetClass, targetClassName, beanName, interceptors, proxyTargetClass);
                        break;
                    // 只通过扫描到接口或者类方法上的注解后，来确定是否要代理
                    case BY_METHOD_ANNOTATION_ONLY:
                        result = scanAndProxyForMethod(targetClass, targetClassName, beanName, interceptors, proxyTargetClass);
                        break;
                    // 上述两者都可以
                    case BY_CLASS_OR_METHOD_ANNOTATION:
                        Object[] classProxyResult = scanAndProxyForClass(targetClass, targetClassName, beanName, interceptors, proxyTargetClass);
                        // 没有接口或类名上扫描到目标注解，那么扫描接口或类的方法上的目标注解
                        Object[] methodProxyResult = scanAndProxyForMethod(targetClass, targetClassName, beanName, interceptors, proxyTargetClass);
                        if (classProxyResult != DO_NOT_PROXY || methodProxyResult != DO_NOT_PROXY) {
                            result = interceptors;
                        } else {
                            result = DO_NOT_PROXY;
                        }
                        break;
                }

                // 是否需要代理
                proxyMap.put(targetClassName, Boolean.valueOf(result != DO_NOT_PROXY));

                if (result != DO_NOT_PROXY) {
                    // 是接口代理还是类代理
                    proxyTargetClassMap.put(beanName, proxyTargetClass);

                    if (result == PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS) {
                        LOG.info("Class [{}] is proxied by common interceptors [{}], proxyTargetClass={}", targetClassName, MatrixUtils.toString(getCommonInterceptors()), proxyTargetClass);
                    } else {
                        LOG.info("Class [{}] is proxied by additional interceptors [{}], proxyTargetClass={}", targetClassName, result, proxyTargetClass);
                    }
                }

                return result;
            }
        }

        return DO_NOT_PROXY;
    }

    protected Object[] scanAndProxyForClass(Class<?> targetClass, String targetClassName, String beanName, Object[] interceptors, boolean proxyTargetClass) {
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
                        // 如果“注解扫描后处理”不开启，没必要再往下执行循环，直接返回
                        return interceptors;
                    }

                    // 目标注解被扫描到，proxied赋值为true，即认为该接口或者类被代理
                    if (!proxied) {
                        proxied = true;
                    }
                }
            }
        }

        return proxied ? interceptors : DO_NOT_PROXY;
    }

    protected Object[] scanAndProxyForMethod(Class<?> targetClass, String targetClassName, String beanName, Object[] interceptors, boolean proxyTargetClass) {
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
                            // 如果“注解扫描后处理”不开启，没必要再往下执行循环，直接返回
                            return interceptors;
                        }

                        // 目标注解被扫描到，proxied赋值为true，即认为该接口或者类被代理
                        if (!proxied) {
                            proxied = true;
                        }
                    }
                }
            }
        }

        return proxied ? interceptors : DO_NOT_PROXY;
    }

    // 获取切面拦截类的方式
    // 1. 根据targetClass从额外拦截类列表中去取（接口代理targetClass是接口类，类代理targetClass是类）
    // 2. 如果从额外拦截类列表中没取到，就从全局拦截类列表中去取（PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS）
    // 3. 如果从全局拦截类列表中没取到，就不代理（DO_NOT_PROXY）
    protected Object[] getInterceptors(Class<?> targetClass) {
        Object[] interceptors = getAdditionalInterceptors(targetClass);
        if (ArrayUtils.isNotEmpty(interceptors)) {
            return interceptors;
        }

        Class<?>[] commonInterceptorClasses = getCommonInterceptors();
        if (ArrayUtils.isNotEmpty(commonInterceptorClasses)) {
            return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
        }

        return DO_NOT_PROXY;
    }

    // 返回具有调用拦截的全局切面实现类，拦截类必须实现MethodInterceptor接口, 可以多个
    // 如果返回null， 全局切面代理关闭
    protected abstract Class<? extends MethodInterceptor>[] getCommonInterceptors();

    // 返回额外的拦截类实例列表，拦截类必须实现MethodInterceptor接口，分别对不同的接口或者类赋予不同的拦截类，可以多个
    // 如果返回null， 额外切面代理关闭
    protected abstract Object[] getAdditionalInterceptors(Class<?> targetClass);

    // 返回接口名或者类名上的注解列表，可以多个, 如果接口名或者类名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
    // 如果返回null，则对列表中的注解不做代理和扫描
    protected abstract Class<? extends Annotation>[] getClassAnnotations();

    // 返回接口或者类的方法名上的注解，可以多个，如果接口或者类中方法名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
    // 如果返回null，则对列表中的注解不做代理和扫描
    protected abstract Class<? extends Annotation>[] getMethodAnnotations();

    // 一旦指定的接口或者类名上的注解被扫描到，将会触发该方法
    protected abstract void classAnnotationScanned(Class<?> targetClass, Class<? extends Annotation> classAnnotation);

    // 一旦指定的接口或者类的方法名上的注解被扫描到，将会触发该方法
    protected abstract void methodAnnotationScanned(Class<?> targetClass, Method method, Class<? extends Annotation> methodAnnotation);
}