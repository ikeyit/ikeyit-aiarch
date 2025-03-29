package com.ikeyit.common.web.exception;

import com.ikeyit.common.data.AllIncludedFields;
import com.ikeyit.common.data.DefaultIncludedFields;
import com.ikeyit.common.data.EmptyIncludedFields;
import com.ikeyit.common.data.IncludedFields;
import org.springframework.core.convert.converter.Converter;


/**
 * Converts string representations of included fields to IncludedFields objects.
 * This converter is used in Spring's conversion service to handle field inclusion specifications
 * in REST API requests.
 *
 * Supports three formats:
 * - Empty string: Converts to EmptyIncludedFields
 * - "*": Converts to AllIncludedFields
 * - Comma-separated field names: Converts to DefaultIncludedFields
 */
public class IncludedFieldsConverter implements Converter<String, IncludedFields> {
    /**
     * Converts a string representation to an IncludedFields object.
     *
     * @param source The string to convert
     * @return An IncludedFields implementation based on the input string
     */
    @Override
    public IncludedFields convert(String source) {
        if (source.isEmpty())
            return EmptyIncludedFields.get();
        if ("*".equals(source))
            return AllIncludedFields.get();

        return DefaultIncludedFields.of(source.split(","));
    }
}