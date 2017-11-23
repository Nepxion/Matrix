package com.nepxion.matrix.test.simple;

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

import com.nepxion.matrix.test.simple.context.MyContextAware;
import com.nepxion.matrix.test.simple.service.MyService1;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nepxion.matrix.test.simple" })
public class MyApplication1 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication1.class, args);

        MyService1 myService1 = MyContextAware.getBean(MyService1.class);
        myService1.doA("A");
        myService1.doB("B");
    }
}