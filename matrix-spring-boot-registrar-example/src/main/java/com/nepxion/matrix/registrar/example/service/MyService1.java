package com.nepxion.matrix.registrar.example.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.matrix.registrar.example.aop.MyAnnotation;

@MyAnnotation(name = "a", label = "b", description = "${myAnno.c:hello}")
public interface MyService1 {
    String doA(String id);

    String doB(String id);
}