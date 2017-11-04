package com.nepxion.matrix.test.simple.service;

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

@Service("myService1Impl")
public class MyService1Impl implements MyService1 {
    @Override
    public void doA(String id) {
        System.out.println("doA");
    }

    @Override
    public void doB(String id) {
        System.out.println("doB");
    }
}