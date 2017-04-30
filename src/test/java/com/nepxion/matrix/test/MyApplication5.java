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

import com.nepxion.matrix.context.MatrixContextAware;
import com.nepxion.matrix.test.service.MyService5Impl;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nepxion.matrix.test" })
public class MyApplication5 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication5.class, args);

        MyService5Impl myService5 = MatrixContextAware.getBean(MyService5Impl.class);
        myService5.doI("I");
        myService5.doJ("J");
    }
}