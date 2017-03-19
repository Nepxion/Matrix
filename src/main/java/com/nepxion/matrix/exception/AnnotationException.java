package com.nepxion.matrix.exception;

/**
 * <p>Title: Nepxion Matrix AOP</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class AnnotationException extends RuntimeException {
    private static final long serialVersionUID = 1121978434845423269L;

    public AnnotationException() {
        super();
    }

    public AnnotationException(String message) {
        super(message);
    }

    public AnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationException(Throwable cause) {
        super(cause);
    }
}