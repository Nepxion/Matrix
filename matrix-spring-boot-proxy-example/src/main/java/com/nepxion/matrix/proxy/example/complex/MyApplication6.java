package com.nepxion.matrix.proxy.example.complex;

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

import com.nepxion.matrix.proxy.example.complex.service.MyService6Impl;

@SpringBootApplication
public class MyApplication6 {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MyApplication6.class, args);

        MyService6Impl myService6 = applicationContext.getBean(MyService6Impl.class);
        myService6.doK("K");
        myService6.doL("L");
    }
}