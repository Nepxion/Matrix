package com.nepxion.matrix.proxy.simple;

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

import com.nepxion.matrix.proxy.simple.context.MyContextAware;
import com.nepxion.matrix.proxy.simple.service.MyService2Impl;

@SpringBootApplication
public class MyApplication2 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication2.class, args);

        MyService2Impl myService2 = MyContextAware.getBean(MyService2Impl.class);
        myService2.doC("C");
        myService2.doD("D");
    }
}