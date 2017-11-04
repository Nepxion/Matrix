package com.nepxion.matrix.test.complex.service;

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

import com.nepxion.matrix.test.complex.aop.MyAnnotation3;
import com.nepxion.matrix.test.complex.aop.MyAnnotation4;

@Service("myService6Impl")
@MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
public class MyService6Impl {
    @MyAnnotation3(name = "MyAnnotation3", label = "MyAnnotation3", description = "MyAnnotation3")
    public void doK(String id) {
        System.out.println("doK");
    }

    public void doL(String id) {
        System.out.println("doL");
    }
}