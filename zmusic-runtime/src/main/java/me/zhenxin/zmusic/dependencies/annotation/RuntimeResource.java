package me.zhenxin.zmusic.dependencies.annotation;

import java.lang.annotation.*;

@SuppressWarnings({"AlibabaClassMustHaveAuthor", "unused"})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RuntimeResources.class)
public @interface RuntimeResource {

    String value();

    String hash();

    String name() default "";

    String tag() default "";

    boolean zip() default false;

}