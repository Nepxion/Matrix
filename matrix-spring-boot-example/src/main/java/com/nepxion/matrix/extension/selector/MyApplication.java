package com.nepxion.matrix.extension.selector;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nepxion.matrix.extension.selector.aop.EnableMyAnnotation;
import com.nepxion.matrix.extension.selector.config.MyBean;
import com.nepxion.matrix.extension.selector.context.MyContextAware;

// 本例展示在Spring Boot入口加上@EnableMyAnnotation，自动初始化对应的Configuration
// 通过spring.factories定义注解对应的配置类，当@EnableMyAnnotation加上，同时application.properties里com.nepxion.myannotation.enabled=true（或者不配置），那么
// MyBean myBean = MyContextAware.getBean(MyBean.class);才能取到
// 使用方式和@EnableCircuitBreaker相似
@SpringBootApplication
@EnableMyAnnotation
public class MyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);
        
        MyBean myBean = MyContextAware.getBean(MyBean.class);
        System.out.println(myBean);
    }
}