package com.ikeyit.common.data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class ReflectionUtils {

    public static ParameterizedType findParameterizedInterface(Class<?> clazz, Class<?> interfaceClazz) {
        for (Type type : clazz.getGenericInterfaces()) {
            Class<?> currentClazz = null;
            if (type instanceof ParameterizedType parameterizedType) {
                currentClazz = (Class<?>) parameterizedType.getRawType();
                if (Objects.equals(currentClazz, interfaceClazz)) {
                    return parameterizedType;
                }
            } else if (type instanceof Class){
                currentClazz = (Class<?>) type;
            }

            if (currentClazz != null) {
                ParameterizedType result = findParameterizedInterface(currentClazz, interfaceClazz);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

}
