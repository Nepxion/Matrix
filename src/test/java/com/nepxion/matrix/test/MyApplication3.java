package com.nepxion.matrix.test;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.nepxion.matrix.test.context.MyContextAware;
import com.nepxion.matrix.test.service.MyService3Impl;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nepxion.matrix.test" })
public class MyApplication3 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication3.class, args);

        MyService3Impl myService3 = MyContextAware.getBean(MyService3Impl.class);
        myService3.doE("E");
        myService3.doF("F");
    }
}