package com.nepxion.matrix.proxy.complex;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nepxion.matrix.proxy.complex.context.MyContextAware;
import com.nepxion.matrix.proxy.complex.service.MyService3;

@SpringBootApplication
public class MyApplication3 {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication3.class, args);

        MyService3 myService3 = MyContextAware.getBean(MyService3.class);
        myService3.doE("E");
        myService3.doF("F");
    }
}