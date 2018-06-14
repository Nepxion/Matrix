package com.nepxion.matrix.proxy.complex.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepxion.matrix.proxy.complex.aop.MyAnnotation5;
import com.nepxion.matrix.proxy.complex.aop.MyAnnotation6;

@Service("myService5Impl")
public class MyService5Impl {
    // 1. 通过自动装配的方式，自身调用自身的注解方法，但似乎在Spring 4.3.X版本里才有效果（在某些Spring版本没这个效果，未做全面调查）
    // 对于低版本的Spring，请使用通过AopContext.currentProxy()的方式，也可以实现自身调用自身的注解方法，达到切面效果
    @Autowired
    private MyService5Impl myService5;

    @MyAnnotation5(name = "MyAnnotation5", label = "MyAnnotation5", description = "MyAnnotation5")
    public void doI(String id) {
        System.out.println("doI");
    }

    @MyAnnotation6(name = "MyAnnotation6", label = "MyAnnotation6", description = "MyAnnotation6")
    public void doJ(String id) {
        // 2. 通过AopContext.currentProxy()，该方式必须实现设置AbstractAutoScanProxy的构造方法中exposeProxy=true
        // MyService5Impl myService5 = (MyService5Impl) AopContext.currentProxy();
        myService5.doI(id);

        System.out.println("doJ");
    }
}