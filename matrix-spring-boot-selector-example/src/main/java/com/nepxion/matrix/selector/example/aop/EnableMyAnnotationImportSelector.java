package com.nepxion.matrix.selector.example.aop;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.type.AnnotationMetadata;

import com.nepxion.matrix.selector.AbstractImportSelector;
import com.nepxion.matrix.selector.RelaxedPropertyResolver;
import com.nepxion.matrix.selector.example.configuration.MyConfigurationExtension;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EnableMyAnnotationImportSelector extends AbstractImportSelector<EnableMyAnnotation> {
    // 如下方法适合EnableXXX注解上带有参数的情形，一般用不到
    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        // 获取父类的Configuration列表
        String[] imports = super.selectImports(metadata);

        // 从注解上获取参数
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(getAnnotationClass().getName(), true));
        boolean extension = attributes.getBoolean("extension");

        if (extension) {
            // 如果EnableMyAnnotation注解上的extension为true，那么去装载MyConfigurationExtension，即初始化里面的MyBeanExtension
            List<String> importsList = new ArrayList<>(Arrays.asList(imports));
            importsList.add(MyConfigurationExtension.class.getCanonicalName());
            imports = importsList.toArray(new String[0]);
        } else {
            // 如果EnableMyAnnotation注解上的extension为false，那么你可以把该参数动态放到属性列表里
            Environment environment = getEnvironment();
            if (ConfigurableEnvironment.class.isInstance(environment)) {
                ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("extension.enabled", false);
                MapPropertySource propertySource = new MapPropertySource("nepxion", map);
                configurableEnvironment.getPropertySources().addLast(propertySource);
            }
        }

        return imports;
    }

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