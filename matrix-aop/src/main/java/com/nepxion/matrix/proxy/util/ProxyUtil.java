package com.nepxion.matrix.proxy.util;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.ArrayUtils;

public class ProxyUtil {
    // 转换Class数组成字符串格式
    public static String toString(Class<?>[] parameterTypes) {
        if (ArrayUtils.isEmpty(parameterTypes)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            builder.append("," + clazz.getCanonicalName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }

    // 转换String数组成字符串格式
    public static String toString(String[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            builder.append("," + value);
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }
}