package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.spring.converter.IntToEnumWithIntConverter;
import com.ikeyit.common.data.spring.converter.JsonToObjectConverter;
import com.ikeyit.common.data.spring.converter.StringToEnumWithIntConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Collection;

public class EnhancedConversionService extends GenericConversionService {

    private static final class InstanceHolder {
        private static final EnhancedConversionService instance = new EnhancedConversionService();
    }

    public static ConversionService getInstance() {
        return InstanceHolder.instance;
    }

    private EnhancedConversionService() {
        DefaultConversionService.addDefaultConverters(this);
        removeConvertible(String.class, Collection.class); // Use JsonToObjectConverter instead
        removeConvertible(Object.class, Collection.class);
        addConverter(new JsonToObjectConverter());
        addConverter(new IntToEnumWithIntConverter());
        addConverter(new StringToEnumWithIntConverter());
    }
}
