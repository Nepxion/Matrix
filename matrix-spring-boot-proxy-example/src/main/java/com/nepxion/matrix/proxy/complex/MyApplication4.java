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

import com.nepxion.matrix.proxy.complex.context.MyContextAware;
import com.nepxion.matrix.proxy.complex.service.MyService4Impl;

@SpringBootApplication
public class MyApplication4 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication4.class, args);

        MyService4Impl myService4 = MyContextAware.getBean(MyService4Impl.class);
        myService4.doG("G");
        myService4.doH("H");
    }
}