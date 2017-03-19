package com.nepxion.matrix.test.service;

/**
 * <p>Title: Nepxion Matrix AOP</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.stereotype.Service;

import com.nepxion.matrix.test.aop.MyAnnotation4;

@Service("myService3Impl")
public class MyService3Impl {
    public void doE(String id) {
        System.out.println("doE");
    }

    @MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
    public void doF(String id) {
        System.out.println("doF");
    }
}