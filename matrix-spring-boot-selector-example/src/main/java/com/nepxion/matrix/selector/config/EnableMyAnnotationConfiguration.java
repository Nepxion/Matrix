package com.nepxion.matrix.selector.config;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.context.annotation.Bean;

// @Configuration
public class EnableMyAnnotationConfiguration {
    @Bean
    public MyBean myBean() {
        return new MyBean("MyBean");
    }
}