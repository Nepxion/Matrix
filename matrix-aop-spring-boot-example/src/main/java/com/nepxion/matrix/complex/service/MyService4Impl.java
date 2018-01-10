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

import com.nepxion.matrix.complex.aop.MyAnnotation4;
import com.nepxion.matrix.complex.aop.MyAnnotation5;
import com.nepxion.matrix.complex.aop.MyAnnotation6;
import com.nepxion.matrix.complex.aop.MyAnnotation7;

@Service("myService4Impl")
@MyAnnotation4(name = "MyAnnotation4", label = "MyAnnotation4", description = "MyAnnotation4")
public class MyService4Impl {
    @MyAnnotation5(name = "MyAnnotation5", label = "MyAnnotation5", description = "MyAnnotation5")
    public void doG(@MyAnnotation7(name = "MyAnnotation7", label = "MyAnnotation7", description = "MyAnnotation7") String id) {
        System.out.println("doG");
    }

    @MyAnnotation6(name = "MyAnnotation6", label = "MyAnnotation6", description = "MyAnnotation6")
    public void doH(String id) {
        System.out.println("doH");
    }
}