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
import com.nepxion.matrix.registrar.aop.MyProxy;
import com.nepxion.matrix.registrar.service.MyService1;
import com.nepxion.matrix.registrar.service.MyService2;
import com.nepxion.matrix.registrar.service.MyService3;

@SpringBootApplication
@EnableMyAnnotation(basePackages = "com.nepxion.matrix.registrar.service")
@ComponentScan(basePackages = { "com.nepxion.matrix.registrar" })
public class MyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);

        MyService1 myService1 = MyProxy.getProxy(MyService1.class);
        System.out.println("调用结果=" + myService1.doA("A"));

        MyService2 myService2 = MyProxy.getProxy(MyService2.class);
        System.out.println("调用结果=" + myService2.doC("C"));

        MyService3 myService3 = MyProxy.getProxy(MyService3.class);
        System.out.println("调用结果=" + myService3.doE("E"));
    }
}