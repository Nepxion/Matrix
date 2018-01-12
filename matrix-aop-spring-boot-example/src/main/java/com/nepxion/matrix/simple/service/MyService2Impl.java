package com.nepxion.matrix.simple.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.stereotype.Service;

import com.nepxion.matrix.simple.aop.MyAnnotation2;

@Service("myService2Impl")
public class MyService2Impl {
    @MyAnnotation2(name = "MyAnnotation2", label = "MyAnnotation2", description = "MyAnnotation2")
    public void doC(String id) {
        System.out.println("doC");
    }

    public void doD(String id) {
        System.out.println("doD");
    }
}