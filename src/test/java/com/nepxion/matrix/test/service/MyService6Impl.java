package com.nepxion.matrix.test.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.stereotype.Service;

import com.nepxion.matrix.test.aop.MyAnnotation6;

@Service("myService6Impl")
public class MyService6Impl {
    @MyAnnotation6(name = "MyAnnotation6", label = "MyAnnotation6", description = "MyAnnotation6")
    public void doK(String id) {
        System.out.println("doK");
    }

    public void doL(String id) {
        System.out.println("doL");
    }
}