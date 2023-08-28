package me.zhenxin.zmusic.dependencies.annotation;

import me.zhenxin.zmusic.dependencies.DependencyScope;

import java.lang.annotation.*;

/**
 * 使用 ! 前缀来避免在编译过程中被 taboolib-gradle-plugin 或 shadowJar 二次重定向。
 */
@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RuntimeDependencies.class)
public @interface RuntimeDependency {

    String value();

    String test() default "";

    String repository() default "https://maven.aliyun.com/repository/public";

    boolean transitive() default true;

    boolean ignoreOptional() default true;

    boolean ignoreException() default false;

    DependencyScope[] scopes() default {DependencyScope.RUNTIME, DependencyScope.COMPILE};

    String[] relocate() default {};

    boolean initiative() default false;

    boolean isolated() default false;

}