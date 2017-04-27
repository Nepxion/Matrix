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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepxion.matrix.test.aop.MyAnnotation4;

@Service("myService3Impl")
public class MyService3Impl {
    // 1. 自身自动装配，只在4.3版本里才有效果
    @Autowired
    private MyService3Impl selfService3;

    @MyAnnotation3(name = "MyAnnotation3", label = "MyAnnotation3", description = "MyAnnotation3")
    public void doE(String id) {
        System.out.println("doE");
    }

    @MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
    public void doF(String id) {
        // 2. 通过AopContext.currentProxy()
        // MyService3Impl selfService3 = (MyService3Impl) AopContext.currentProxy();
        selfService3.doE(id);

        System.out.println("doF");
    }
}