package com.nepxion.matrix.mode;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public enum ScanMode {
    // 只执行扫描到接口名或者类名上的注解后的处理
    FOR_CLASS_ANNOTATION_ONLY,
    // 只执行扫描到接口或者类方法上的注解后的处理
    FOR_METHOD_ANNOTATION_ONLY,
    // 上述两者都执行
    FOR_CLASS_OR_METHOD_ANNOTATION
}