package com.nepxion.matrix.simple;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.nepxion.matrix.simple.context.MyContextAware;
import com.nepxion.matrix.simple.service.MyService1;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nepxion.matrix.simple" })
public class MyApplication1 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication1.class, args);

        MyService1 myService1 = MyContextAware.getBean(MyService1.class);
        myService1.doA("A");
        myService1.doB("B");
    }
}