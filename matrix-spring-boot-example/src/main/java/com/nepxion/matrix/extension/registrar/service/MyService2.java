package com.nepxion.matrix.extension.registrar.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.matrix.extension.registrar.aop.MyAnnotation;

@MyAnnotation(name = "x", label = "y", description = "z")
public interface MyService2 {
    String doC(String id);

    String doD(String id);
}