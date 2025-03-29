package com.ikeyit.common.data.spring.converter;

import com.ikeyit.common.data.EnumWithInt;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A Spring converter that converts String values to Enum types implementing the EnumWithInt interface.
 * This converter specifically handles cases where string representations of integer values need to be
 * converted to enum constants that have associated integer values.
 */
public class StringToEnumWithIntConverter implements ConditionalGenericConverter {
    /**
     * Determines if this converter can convert between the source and target types.
     * Checks if the target type is an enum that implements EnumWithInt interface.
     *
     * @param sourceType The type descriptor of the source type
     * @param targetType The type descriptor of the target type
     * @return true if conversion is supported, false otherwise
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.isAssignableTo(TypeDescriptor.valueOf(EnumWithInt.class)) && targetType.getType().isEnum();
    }

    /**
     * Specifies the source and target types this converter supports.
     * This converter handles String to EnumWithInt conversions.
     *
     * @return A set containing the supported convertible type pair
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, EnumWithInt.class));
    }

    /**
     * Converts a string value to an enum constant based on its integer value.
     * The string must be parseable as an integer that matches an enum constant's value.
     *
     * @param source The source object to convert (expected to be a String)
     * @param sourceType The type descriptor of the source type
     * @param targetType The type descriptor of the target enum type
     * @return The matching enum constant, or null if source is null
     * @throws IllegalStateException if the string cannot be parsed as an integer or no matching enum constant is found
     */
    @Override
    @Nullable
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null)
            return null;
        try {
            int intValue = Integer.parseInt((String) source);
            for (Object o : targetType.getType().getEnumConstants()) {
                if (Objects.equals(((EnumWithInt) o).value(), intValue)) {
                    return o;
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException(source + " can't be mapped to EnumWithInt!", e);
        }

        throw new IllegalStateException(source + " can't be mapped to EnumWithInt!");
    }
}
