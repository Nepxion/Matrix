package com.nepxion.matrix.selector.example.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.nepxion.matrix.selector.AbstractImportSelector;
import com.nepxion.matrix.selector.RelaxedPropertyResolver;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EnableMyAnnotationImportSelector extends AbstractImportSelector<EnableMyAnnotation> {
    @Override
    protected boolean isEnabled() {
        // Spring boot 1.x.x版本的用法
        return new RelaxedPropertyResolver(getEnvironment()).getProperty("com.nepxion.myannotation.enabled", Boolean.class, Boolean.TRUE);

        // Spring boot 2.x.x版本的用法
        // return getEnvironment().getProperty("com.nepxion.myannotation.enabled", Boolean.class, Boolean.TRUE);
    }

    // @Override
    // protected boolean hasDefaultFactory() {
    //    return true;
    // }
}