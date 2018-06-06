package com.nepxion.matrix.registrar.invoker;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nepxion.matrix.registrar.service.MyService1;
import com.nepxion.matrix.registrar.service.MyService2;

@Component
public class MyInvoker {
    @Autowired
    private MyService1 myService1;
    
    @Autowired
    private MyService2 myService2;
    
    public void invokeMyService1() {
        myService1.doA("A");
    }
    
    public void invokeMyService2() {
        myService2.doC("C");
    }
}