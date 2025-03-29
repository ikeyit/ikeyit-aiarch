package com.ikeyit.common.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field or method as representing an embedded object.
 * Used to indicate that the annotated element's properties should be treated as part of the containing class,
 * typically for persistence or serialization purposes.
 *
 * <p>This annotation can be applied to fields and methods, and supports configuration
 * of property name prefixing and nullability.</p>
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Embedded {
    /**
     * Specifies a prefix to be applied to all property names of the embedded object.
     * This can be used to avoid naming conflicts when multiple embedded objects are present.
     *
     * @return the prefix to apply to property names (empty string by default)
     */
    String prefix() default "";

    /**
     * Specifies whether the embedded object can be null.
     *
     * @return true if the embedded object can be null, false otherwise
     */
    boolean nullable() default false;
}
