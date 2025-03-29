package com.ikeyit.common.data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Utility class for reflection operations.
 */
public class ReflectionUtils {

    /**
     * Finds a parameterized interface in the class hierarchy.
     * 
     * @param clazz The class to search in
     * @param interfaceClazz The interface class to find
     * @return The parameterized type if found, otherwise null
     */
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
