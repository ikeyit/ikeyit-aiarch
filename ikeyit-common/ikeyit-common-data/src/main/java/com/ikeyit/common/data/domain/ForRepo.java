package com.ikeyit.common.data.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods, types, or constructors that should only be used by repository implementations.
 * This is a source-level annotation that helps enforce architectural boundaries by indicating that
 * the annotated element is part of the repository layer's internal implementation details.
 * It serves as documentation and can be used by static analysis tools to enforce layer separation.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface ForRepo {
}
