package com.nepxion.matrix.selector.example.configuration;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.matrix.selector.example.bean.MyBeanExtension;

@Configuration
public class MyConfigurationExtension {
    @Bean
    public MyBeanExtension myBeanExtension() {
        return new MyBeanExtension("myBeanExtension");
    }
}