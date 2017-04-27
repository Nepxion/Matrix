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

import com.nepxion.matrix.test.aop.MyAnnotation3;
import com.nepxion.matrix.test.aop.MyAnnotation4;

@Service("myService3Impl")
public class MyService3Impl {
    // 1. 通过自动装配的方式，自身调用自身的注解方法，但似乎在Spring 4.3.X版本里才有效果（在某些Spring版本没这个效果，未做全面调查）
    // 如果没这个效果，请使用通过AopContext.currentProxy()的方式，也可以实现自身调用自身的注解方法，达到切面效果
    @Autowired
    private MyService3Impl myService3;

    @MyAnnotation3(name = "MyAnnotation3", label = "MyAnnotation3", description = "MyAnnotation3")
    public void doE(String id) {
        System.out.println("doE");
    }

    @MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
    public void doF(String id) {
        // 2. 通过AopContext.currentProxy()，该方式必须实现设置AbstractAutoScanProxy的构造方法中exposeProxy=true
        // MyService3Impl myService3 = (MyService3Impl) AopContext.currentProxy();
        myService3.doE(id);

        System.out.println("doF");
    }
}