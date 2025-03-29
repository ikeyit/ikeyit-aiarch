package com.ikeyit.common.data.spring.converter;

import com.ikeyit.common.data.EnumWithInt;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class IntToEnumWithIntConverter implements ConditionalGenericConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.isAssignableTo(TypeDescriptor.valueOf(EnumWithInt.class)) && targetType.getType().isEnum();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Integer.class, EnumWithInt.class));
    }

    @Override
    @Nullable
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null)
            return null;

        for (Object o : targetType.getType().getEnumConstants()) {
            if (Objects.equals(((EnumWithInt) o).value(), source)) {
                return o;
            }
        }
        throw new IllegalStateException(source + " can't be mapped to enum!");
    }
}
