package com.nepxion.matrix.registrar;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Haojun Ren
 * @version 1.0
 */

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class RegistrarFactoryBean implements ApplicationContextAware, FactoryBean<Object>, InitializingBean, BeanClassLoaderAware, EnvironmentAware {
	protected ApplicationContext applicationContext;
	protected Class<?> interfaze;
	protected MethodInterceptor interceptor;
	protected Object proxy;
	protected ClassLoader classLoader;
	protected Environment environment;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public Object getObject() throws Exception {
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaze;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.addInterface(interfaze);
		proxyFactory.addAdvice(interceptor);
		proxyFactory.setOptimize(false);

		proxy = proxyFactory.getProxy(classLoader);
		resolveVariable(this.environment);
	}

	protected void resolveVariable(Environment environment) {
	}

	protected String resolve(String value) {
		if (StringUtils.hasText(value)) {
			return this.environment.resolvePlaceholders(value);
		}
		return value;
	}

	protected String resolveUrl(String url) {
		url = resolve(url);
		if (StringUtils.hasText(url) && !(url.startsWith("#{") && url.contains("}"))) {
			if (!url.contains("://")) {
				url = "http://" + url;
			}
			try {
				new URL(url);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(url + " is malformed", e);
			}
		}
		return url;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Class<?> getInterfaze() {
		return interfaze;
	}

	public void setInterfaze(Class<?> interfaze) {
		this.interfaze = interfaze;
	}

	public MethodInterceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(MethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}

}