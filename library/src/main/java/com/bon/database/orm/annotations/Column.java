package com.bon.database.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dang on 4/4/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * @return the desired name of the column representing the field
     */
    String value();

    boolean treatNullAsDefault() default false;

    boolean readonly() default false;
}