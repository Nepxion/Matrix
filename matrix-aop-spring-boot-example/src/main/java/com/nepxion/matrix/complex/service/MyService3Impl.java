package com.nepxion.matrix.complex.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.stereotype.Service;

@Service("myService3Impl")
public class MyService3Impl implements MyService3 {
    @Override
    public void doE(String id) {
        System.out.println("doE");
    }

    @Override
    public void doF(String id) {
        System.out.println("doF");
    }
}