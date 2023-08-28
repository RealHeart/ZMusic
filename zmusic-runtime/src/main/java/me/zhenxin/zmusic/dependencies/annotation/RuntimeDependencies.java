package me.zhenxin.zmusic.dependencies.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RuntimeDependencies {

    RuntimeDependency[] value();

}