package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.JsonField;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal Use!
 */
class ReflectionSupport {

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
