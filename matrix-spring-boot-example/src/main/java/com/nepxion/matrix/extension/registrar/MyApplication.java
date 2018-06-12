package com.nepxion.matrix.extension.registrar;

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
import org.springframework.context.annotation.ComponentScan;

import com.nepxion.matrix.extension.registrar.aop.EnableMyAnnotation;
import com.nepxion.matrix.extension.registrar.context.MyContextAware;
import com.nepxion.matrix.extension.registrar.invoker.MyInvoker;

// 本例展示在Spring Boot入口加上@EnableMyAnnotation，在接口（不需要实现类）加上MyAnnotation，就可以实现调用拦截的功能
// MySerivice1, MySerivice2头部都加有@EnableMyAnnotation注解，执行MyInterceptor的切面调用
// MySerivice3虽然头部加有注解，但它有实现类，那么优先执行实现类的注入，MyInterceptor的切面调用将不起效果
// 使用方式和@FeignClient相似
@SpringBootApplication
@EnableMyAnnotation(basePackages = "com.nepxion.matrix.extension.registrar.service")
@ComponentScan(basePackages = { "com.nepxion.matrix.extension.registrar" })
public class MyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);

        MyInvoker myInvoker = MyContextAware.getBean(MyInvoker.class);
        System.out.println("调用MyServier1，返回值=" + myInvoker.invokeMyService1());
        System.out.println("调用MyServier2，返回值=" + myInvoker.invokeMyService2());
        System.out.println("调用MyServier3，返回值=" + myInvoker.invokeMyService3());
    }
}