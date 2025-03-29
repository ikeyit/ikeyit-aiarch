package com.ikeyit.common.web.exception;

import com.ikeyit.common.data.AllIncludedFields;
import com.ikeyit.common.data.DefaultIncludedFields;
import com.ikeyit.common.data.EmptyIncludedFields;
import com.ikeyit.common.data.IncludedFields;
import org.springframework.core.convert.converter.Converter;


public class IncludedFieldsConverter implements Converter<String, IncludedFields> {
    @Override
    public IncludedFields convert(String source) {
        if (source.isEmpty())
            return EmptyIncludedFields.get();
        if ("*".equals(source))
            return AllIncludedFields.get();

        return DefaultIncludedFields.of(source.split(","));
    }
}