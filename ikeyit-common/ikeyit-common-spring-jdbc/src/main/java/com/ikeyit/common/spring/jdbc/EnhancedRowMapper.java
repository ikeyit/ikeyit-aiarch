package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.Embedded;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Support embedded entity mapping.
 * @param <T>
 */
public class EnhancedRowMapper<T> implements RowMapper<T> {

    private static final Object NO_DESCRIPTOR = new Object();

    private final Class<?> mappedClass;

    private final ConversionService conversionService;

    private Constructor<?> mappedConstructor;

    private String[] constructorParameterNames;

    private TypeDescriptor[] constructorParameterTypes;

    private final Map<String, Object> propertyDescriptors = new ConcurrentHashMap<>();

    private Map<String, EnhancedRowMapper<?>> embeddedMappers;

    private Map<String, ColumnMapper<T>> columnMappers;

    private final String prefix;

    private final Object writerDesc;

    private final String name;

    private final boolean nullable;

    /**
     * Create a new {@code SimplePropertyRowMapper}.
     * @param mappedClass the class that each row should be mapped to
     */
    public EnhancedRowMapper(Class<T> mappedClass) {
        this(mappedClass, EnhancedConversionService.getInstance());
    }

    /**
     * Create a new {@code SimplePropertyRowMapper}.
     * @param mappedClass the class that each row should be mapped to
     * @param conversionService a {@link ConversionService} for binding
     * JDBC values to bean properties
     */
    public EnhancedRowMapper(Class<T> mappedClass, ConversionService conversionService) {
        Assert.notNull(mappedClass, "Mapped Class must not be null");
        Assert.notNull(conversionService, "ConversionService must not be null");
        this.mappedClass = mappedClass;
        this.conversionService = conversionService;
        this.prefix = "";
        this.writerDesc = null;
        this.name = null;
        this.nullable = false;
        init();
    }

    private EnhancedRowMapper(Field field, String parentPrefix, Embedded embeddedAnnotation, ConversionService conversionService) {
        this.name = field.getName();
        this.prefix = buildPrefix(parentPrefix, embeddedAnnotation.prefix());
        this.writerDesc = field;
        this.mappedClass = field.getType();
        this.conversionService = conversionService;
        this.nullable = embeddedAnnotation.nullable();
        init();
    }

    private EnhancedRowMapper(PropertyDescriptor propertyDescriptor, String parentPrefix, Embedded embeddedAnnotation, ConversionService conversionService) {
        this.name = propertyDescriptor.getName();
        this.prefix = buildPrefix(parentPrefix, embeddedAnnotation.prefix());
        this.writerDesc = propertyDescriptor;
        this.mappedClass = BeanUtils.getWriteMethodParameter(propertyDescriptor).getParameterType();
        this.conversionService = conversionService;
        this.nullable = embeddedAnnotation.nullable();
        init();
    }

    private String buildPrefix(String parentPrefix, String prefix) {
        return parentPrefix + (prefix == null || prefix.isEmpty() ? JdbcUtils.convertPropertyNameToUnderscoreName(this.name) + "_" : prefix);
    }

    private void init() {
        this.mappedConstructor = BeanUtils.getResolvableConstructor(mappedClass);
        int paramCount = this.mappedConstructor.getParameterCount();
        this.constructorParameterNames = (paramCount > 0 ?
            BeanUtils.getParameterNames(this.mappedConstructor) : new String[0]);
        this.constructorParameterTypes = new TypeDescriptor[paramCount];
        for (int i = 0; i < paramCount; i++) {
            this.constructorParameterTypes[i] = new TypeDescriptor(new MethodParameter(this.mappedConstructor, i));
        }

        ReflectionUtils.doWithFields(mappedClass, field -> {
            Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);
            if (embeddedAnnotation != null) {
                addEmbeddedMapper(new EnhancedRowMapper<>(field, prefix, embeddedAnnotation, conversionService));
            }
        });

        for (var propertyDescriptor : BeanUtils.getPropertyDescriptors(mappedClass)) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod == null)
                continue;
            Embedded embeddedAnnotation = writeMethod.getAnnotation(Embedded.class);
            Method readMethod = propertyDescriptor.getReadMethod();
            if (embeddedAnnotation == null) {
                embeddedAnnotation = readMethod == null ? null : readMethod.getAnnotation(Embedded.class);
            }
            if (embeddedAnnotation != null) {
                addEmbeddedMapper(new EnhancedRowMapper<>(propertyDescriptor, prefix, embeddedAnnotation, conversionService));
            }
        }
    }

    private void addEmbeddedMapper(EnhancedRowMapper<?> embeddedMapper) {
        if (embeddedMappers == null) {
            embeddedMappers = new HashMap<>();
        }
        embeddedMappers.put(embeddedMapper.name, embeddedMapper);
    }

    public EnhancedRowMapper<T> addColumnMapper(String columnName, ColumnMapper<T> columnMapper) {
        if (this.columnMappers == null) {
            this.columnMappers = new HashMap<>();
        }
        this.columnMappers.put(columnName, columnMapper);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Map<String, Object> embeddedValues = null;
        if (embeddedMappers != null) {
            embeddedValues = new HashMap<>(embeddedMappers.size());
            for (var entry : embeddedMappers.entrySet()) {
                embeddedValues.put(entry.getKey(), entry.getValue().mapRow(rs, rowNumber));
            }
        }

        Set<Integer> usedIndex = new HashSet<>();
        Object[] args = new Object[this.constructorParameterNames.length];
        boolean allNull = true;
        for (int i = 0; i < args.length; i++) {
            String paramName = this.constructorParameterNames[i];
            if (embeddedValues != null && embeddedValues.containsKey(paramName)) {
                args[i] = embeddedValues.get(paramName);
                // It's used, remove the embedded value
                embeddedValues.remove(paramName);
            } else {
                String name = prefix + paramName;
                int index;
                try {
                    // Try direct name match first
                    index = rs.findColumn(name);
                }
                catch (SQLException ex) {
                    // Try underscored name match instead
                    index = rs.findColumn(JdbcUtils.convertPropertyNameToUnderscoreName(name));
                }
                TypeDescriptor td = this.constructorParameterTypes[i];
                Object value = JdbcUtils.getResultSetValue(rs, index, td.getType());
                usedIndex.add(index);
                args[i] = this.conversionService.convert(value, td);
            }
            if (args[i] != null) {
                allNull = false;
            }
        }
        T mappedObject = (T) BeanUtils.instantiateClass(this.mappedConstructor, args);

        if (!this.mappedClass.isRecord()) {
            // If it's not record, we set other fields or setter.
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int index = 1; index <= columnCount; index++) {
                if (!usedIndex.contains(index)) {
                    String colName = JdbcUtils.lookupColumnName(rsmd, index);
                    if (!StringUtils.startsWithIgnoreCase(colName, prefix)) {
                        continue;
                    }
                    colName = colName.substring(prefix.length());
                    ColumnMapper<T> colMapper = this.columnMappers == null ? null : this.columnMappers.get(colName);
                    if (colMapper != null) {
                        colMapper.accept(mappedObject, rs, index);
                        continue;
                    }
                    Object desc = getDescriptor(colName);
                    if (desc instanceof PropertyDescriptor mp) {
                        var writeMethod = mp.getWriteMethod();
                        var property = new Property(this.mappedClass, mp.getReadMethod(), writeMethod, mp.getName());
                        Object value = JdbcUtils.getResultSetValue(rs, index);
                        value = this.conversionService.convert(value, new TypeDescriptor(property));
                        ReflectionUtils.makeAccessible(writeMethod);
                        ReflectionUtils.invokeMethod(writeMethod, mappedObject, value);
                        if (value != null) {
                            allNull = false;
                        }
                    } else if (desc instanceof Field field) {
                        Object value = JdbcUtils.getResultSetValue(rs, index);
                        value = this.conversionService.convert(value, new TypeDescriptor(field));
                        ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, mappedObject, value);
                        if (value != null) {
                            allNull = false;
                        }
                    }
                }
            }
        }

        if (embeddedValues != null) {
            for (var entry : embeddedValues.entrySet()) {
                Object value = entry.getValue();
                embeddedMappers.get(entry.getKey()).assignTo(value, mappedObject);
                if (value != null) {
                    allNull = false;
                }
            }
        }

        return nullable && allNull ? null : mappedObject;
    }


    private void assignTo(Object embeddedObject, Object object) {
        if (writerDesc instanceof PropertyDescriptor propertyDescriptor) {
            Method method = propertyDescriptor.getWriteMethod();
            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, object, embeddedObject);
        } else if (writerDesc instanceof Field field) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, object, embeddedObject);
        }
    }

    private Object getDescriptor(String column) {
        return this.propertyDescriptors.computeIfAbsent(column, name -> {
            // Try direct match first
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(this.mappedClass, name);
            if (pd != null && pd.getWriteMethod() != null) {
                return pd;
            }
            Field field = ReflectionUtils.findField(this.mappedClass, name);
            if (field != null) {
                return field;
            }

            // Try de-underscored match instead
            String adaptedName = JdbcUtils.convertUnderscoreNameToPropertyName(name);
            if (!adaptedName.equals(name)) {
                pd = BeanUtils.getPropertyDescriptor(this.mappedClass, adaptedName);
                if (pd != null && pd.getWriteMethod() != null) {
                    return pd;
                }
                field = ReflectionUtils.findField(this.mappedClass, adaptedName);
                if (field != null) {
                    return field;
                }
            }
            return NO_DESCRIPTOR;
        });
    }

}
