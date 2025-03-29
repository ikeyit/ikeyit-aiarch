package com.ikeyit.common.data;

import java.lang.annotation.*;

/**
 * Annotate a property of bean as json string to serialize and deserialize
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {

}
