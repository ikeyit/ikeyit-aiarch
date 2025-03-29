package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.spring.converter.IntToEnumWithIntConverter;
import com.ikeyit.common.data.spring.converter.JsonToObjectConverter;
import com.ikeyit.common.data.spring.converter.StringToEnumWithIntConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Collection;

/**
 * An enhanced implementation of Spring's ConversionService that provides additional converters
 * for handling JSON, enum types, and other custom conversions. This service extends the default
 * Spring conversion service while adding specialized converters for specific data types.
 */
public class EnhancedConversionService extends GenericConversionService {

    /**
     * Holder class for lazy initialization of the singleton instance.
     */
    private static final class InstanceHolder {
        private static final EnhancedConversionService instance = new EnhancedConversionService();
    }

    /**
     * Returns the singleton instance of EnhancedConversionService.
     *
     * @return the shared ConversionService instance
     */
    public static ConversionService getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * Private constructor that initializes the conversion service with default converters
     * and additional custom converters for JSON and enum handling.
     */
    private EnhancedConversionService() {
        DefaultConversionService.addDefaultConverters(this);
        removeConvertible(String.class, Collection.class); // Use JsonToObjectConverter instead
        removeConvertible(Object.class, Collection.class);
        addConverter(new JsonToObjectConverter());
        addConverter(new IntToEnumWithIntConverter());
        addConverter(new StringToEnumWithIntConverter());
    }
}
