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

import com.nepxion.matrix.test.simple.aop.MyAnnotation1;

@MyAnnotation1(name = "MyAnnotation1", label = "MyAnnotation1", description = "MyAnnotation1")
public interface MyService1 {
    void doA(String id);

    void doB(String id);
}