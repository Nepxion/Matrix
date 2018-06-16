package com.nepxion.matrix.proxy.complex;

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
import org.springframework.context.ConfigurableApplicationContext;

import com.nepxion.matrix.proxy.complex.service.MyService5Impl;

@SpringBootApplication
public class MyApplication5 {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MyApplication5.class, args);

        MyService5Impl myService5 = applicationContext.getBean(MyService5Impl.class);
        myService5.doI("E");
        myService5.doJ("F");
    }
}