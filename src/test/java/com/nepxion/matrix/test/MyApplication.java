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
import com.nepxion.matrix.test.service.MyService1;
import com.nepxion.matrix.test.service.MyService2Impl;
import com.nepxion.matrix.test.service.MyService3Impl;
import com.nepxion.matrix.test.service.MyService4Impl;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nepxion.matrix.test" })
public class MyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);

        MyService1 myService1 = MyContextAware.getBean(MyService1.class);
        myService1.doA("A");
        myService1.doB("B");

        MyService2Impl myService2 = MyContextAware.getBean(MyService2Impl.class);
        myService2.doC("C");
        myService2.doD("D");

        MyService3Impl myService3 = MyContextAware.getBean(MyService3Impl.class);
        myService3.doE("E");
        myService3.doF("F");
        
        MyService4Impl myService4 = MyContextAware.getBean(MyService4Impl.class);
        myService4.doG("G");
        myService4.doH("H");
    }
}