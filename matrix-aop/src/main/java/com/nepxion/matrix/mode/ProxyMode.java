package com.nepxion.matrix.mode;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public enum ProxyMode {
    // 只通过扫描到接口名或者类名上的注解后，来确定是否要代理
    BY_CLASS_ANNOTATION_ONLY,
    // 只通过扫描到接口或者类方法上的注解后，来确定是否要代理
    BY_METHOD_ANNOTATION_ONLY,
    // 上述两者都可以
    BY_CLASS_OR_METHOD_ANNOTATION
}