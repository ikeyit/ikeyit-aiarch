package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.JsonField;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for reflection-based operations in the Spring JDBC module.
 * This class provides helper methods for working with annotations and reflection.
 * For internal use within the framework only.
 */
class ReflectionSupport {

    /**
     * Retrieves all field and property names that are annotated with @JsonField in a given class.
     * This method scans both fields directly and properties through their getter/setter methods.
     *
     * @param mappedClass the class to scan for JsonField annotations
     * @return a Set of field/property names that are annotated with @JsonField
     */
    public static Set<String> getAnnotatedJsonFields(Class<?> mappedClass) {
        Set<String> allJsonFields = new HashSet<>();
        ReflectionUtils.doWithFields(mappedClass, field -> {
            JsonField jsonField = field.getAnnotation(JsonField.class);
            if (jsonField != null) {
                allJsonFields.add(field.getName());
            }
        });

        for (var p : BeanUtils.getPropertyDescriptors(mappedClass)) {
            JsonField jsonField = null;
            Method method = p.getReadMethod();
            if (method != null) {
                jsonField = method.getAnnotation(JsonField.class);
            }
            if (jsonField == null) {
                method = p.getWriteMethod();
            }
            if (method != null) {
                jsonField = method.getAnnotation(JsonField.class);
            }
            if (jsonField != null) {
                allJsonFields.add(p.getName());
            }
        }
        return allJsonFields;
    }

}
