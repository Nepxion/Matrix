package com.nepxion.matrix.registrar;

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

import com.nepxion.matrix.registrar.aop.EnableMyAnnotation;
import com.nepxion.matrix.registrar.context.MyContextAware;
import com.nepxion.matrix.registrar.invoker.MyInvoker;

@SpringBootApplication
@EnableMyAnnotation(basePackages = "com.nepxion.matrix.registrar.service")
@ComponentScan(basePackages = { "com.nepxion.matrix.registrar" })
public class MyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);

        MyInvoker myInvoker = MyContextAware.getBean(MyInvoker.class);
        System.out.println("返回值：" + myInvoker.invokeMyService1());
        System.out.println("返回值：" + myInvoker.invokeMyService2());
        System.out.println("返回值：" + myInvoker.invokeMyService3());
    }
}