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

import com.nepxion.matrix.test.aop.MyAnnotation2;
import com.nepxion.matrix.test.aop.MyAnnotation3;
import com.nepxion.matrix.test.aop.MyAnnotation4;

@Service("myService2Impl")
@MyAnnotation2(name = "MyAnnotation2", label = "MyAnnotation2", description = "MyAnnotation2")
public class MyService2Impl {
    @MyAnnotation3(name = "MyAnnotation3", label = "MyAnnotation3", description = "MyAnnotation3")
    public void doC(String id) {
        System.out.println("doC");
    }

    @MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
    public void doD(String id) {
        System.out.println("doD");
    }
}