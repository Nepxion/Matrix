package com.nepxion.matrix.registrar.service;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary // 优先Bean注入，用来代替MyService3后置切面拦截调用
public class MyService3Impl implements MyService3 {
    public String doE(String id) {
        return "直接返回 " + id;
    }

    public String doF(String id) {
        return "直接返回 " + id;
    }
}