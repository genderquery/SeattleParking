package com.github.genderquery.moshi;

import com.squareup.moshi.JsonQualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Supports JSON values written as delimited lists.
 */
@Target(FIELD)
@Retention(RUNTIME)
@JsonQualifier
@Documented
public @interface Split {
    String delimiter();
}
