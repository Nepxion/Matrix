package com.nepxion.matrix.selector;

/**
 * <p>Title: Nepxion Matrix</p>
 * <p>Description: Nepxion Matrix AOP</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.matrix.constant.MatrixConstant;
import com.taobao.text.Color;

public abstract class AbstractImportSelector<T> implements DeferredImportSelector, BeanClassLoaderAware, EnvironmentAware {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractImportSelector.class);

    static {
        /*String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
        if (Boolean.valueOf(bannerShown)) {
            System.out.println("");
            System.out.println("╔═╗╔═╗   ╔╗");
            System.out.println("║║╚╝║║  ╔╝╚╗");
            System.out.println("║╔╗╔╗╠══╬╗╔╬═╦╦╗╔╗");
            System.out.println("║║║║║║╔╗║║║║╔╬╬╬╬╝");
            System.out.println("║║║║║║╔╗║║╚╣║║╠╬╬╗");
            System.out.println("╚╝╚╝╚╩╝╚╝╚═╩╝╚╩╝╚╝");
            System.out.println("Nepxion Matrix - Import Selector  v" + MatrixConstant.MATRIX_VERSION);
            System.out.println("");
        }*/

        LogoBanner logoBanner = new LogoBanner(AbstractImportSelector.class, "/com/nepxion/matrix/resource/logo.txt", "Welcome to Nepxion", 6, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta }, true);

        NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", MatrixConstant.MATRIX_VERSION, 0, 1), new Description(BannerConstant.PLUGIN + ":", "Import Selector", 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/matrix", 0, 1));
    }

    private ClassLoader beanClassLoader;
    private Class<T> annotationClass;
    private Environment environment;

    @SuppressWarnings("unchecked")
    protected AbstractImportSelector() {
        this.annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractImportSelector.class);
    }

    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        if (!isEnabled()) {
            return new String[0];
        }

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(this.annotationClass.getName(), true));

        Assert.notNull(attributes, "No " + getSimpleName() + " attributes found. Is " + metadata.getClassName() + " annotated with @" + getSimpleName() + "?");

        // Find all possible auto configuration classes, filtering duplicates
        List<String> factories = new ArrayList<>(new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(this.annotationClass, this.beanClassLoader)));

        if (factories.isEmpty() && !hasDefaultFactory()) {
            throw new IllegalStateException("Annotation @" + getSimpleName() + " found, but there are no implementations. Did you forget to include a starter?");
        }

        if (factories.size() > 1) {
            // there should only ever be one DiscoveryClient, but there might be more than one factory
            LOG.warn("More than one implementation " + "of @" + getSimpleName() + " (now relying on @Conditionals to pick one): " + factories);
        }

        return factories.toArray(new String[factories.size()]);
    }

    protected boolean hasDefaultFactory() {
        return false;
    }

    protected String getSimpleName() {
        return this.annotationClass.getSimpleName();
    }

    protected Class<T> getAnnotationClass() {
        return this.annotationClass;
    }

    protected Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    protected abstract boolean isEnabled();
}