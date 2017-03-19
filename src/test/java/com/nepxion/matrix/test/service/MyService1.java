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

import com.nepxion.matrix.test.aop.MyAnnotation1;
import com.nepxion.matrix.test.aop.MyAnnotation2;
import com.nepxion.matrix.test.aop.MyAnnotation3;
import com.nepxion.matrix.test.aop.MyAnnotation4;

@MyAnnotation1(name = "MyAnnotation1", label = "MyAnnotation1", description = "MyAnnotation1")
@MyAnnotation2(name = "MyAnnotation2", label = "MyAnnotation2", description = "MyAnnotation2")
public interface MyService1 {
    @MyAnnotation3(name = "MyAnnotation3", label = "MyAnnotation3", description = "MyAnnotation3")
    void doA(String id);

    @MyAnnotation3(name = "MyAnnotation3", label = "MyAnnotation3", description = "MyAnnotation3")
    @MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
    void doB(String id);
}