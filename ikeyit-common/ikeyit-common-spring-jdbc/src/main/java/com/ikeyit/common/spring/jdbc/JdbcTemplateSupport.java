package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.domain.Entity;
import org.springframework.core.ResolvableType;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public class JdbcTemplateSupport {
    public static <ID, A extends Entity<ID>> void createWithGeneratedKey(NamedParameterJdbcTemplate template,
                                                                       String sql,
                                                                       A entity,
                                                                       Function<A, SqlParameterSource> sqlParameterSourceBuilder) {
        createWithGeneratedKey(template, sql, entity, sqlParameterSourceBuilder, "id");
    }

    public static <ID, A extends Entity<ID>> void createWithGeneratedKey(NamedParameterJdbcTemplate template,
                                                                       String sql,
                                                                       A entity,
                                                                       Function<A, SqlParameterSource> sqlParameterSourceBuilder,
                                                                       String keyColumn) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int result = template.update(sql, sqlParameterSourceBuilder.apply(entity), keyHolder, new String[] {keyColumn});
        Assert.isTrue(result == 1, "Insert sql returns 0 success!");
        assignId(entity, keyHolder.getKey());
    }

    @SuppressWarnings("all")
    private static <ID, A extends Entity<ID>> void assignId(A entity, Number key) {
        ResolvableType entityType = ResolvableType.forInstance(entity);
        Type genericType = entityType.as(Entity.class).getGeneric(0).resolve();
        if (genericType == Long.class) {
            entity.assignId((ID) Long.valueOf(key.longValue()));
        } else if (genericType == Integer.class) {
            entity.assignId((ID) Integer.valueOf(key.intValue()));
        } else if (genericType == String.class) {
            entity.assignId((ID) key.toString());
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass().getName());
        }
    }
    public static <ID, A extends Entity<ID>> void batchCreate(NamedParameterJdbcTemplate template,
                                                                            String sql,
                                                                            Collection<A> entities,
                                                                            Function<A, SqlParameterSource> sqlParameterSourceBuilder) {
        SqlParameterSource[] parameters = entities.stream()
            .map(sqlParameterSourceBuilder)
            .toArray(SqlParameterSource[]::new);
        template.batchUpdate(sql, parameters);
    }

    public static <ID, A extends Entity<ID>> void batchCreateWithGeneratedKey(NamedParameterJdbcTemplate template,
                                                                            String sql,
                                                                            Collection<A> entities,
                                                                            Function<A, SqlParameterSource> sqlParameterSourceBuilder) {
        batchCreateWithGeneratedKey(template, sql, entities, sqlParameterSourceBuilder, "id");
    }

    public static <ID, A extends Entity<ID>>  void batchCreateWithGeneratedKey(NamedParameterJdbcTemplate template,
                                                                            String sql,
                                                                            Collection<A> entities,
                                                                            Function<A, SqlParameterSource> sqlParameterSourceBuilder,
                                                                            String keyColumn) {
        SqlParameterSource[] parameters = entities.stream()
            .map(sqlParameterSourceBuilder)
            .toArray(SqlParameterSource[]::new);
        batchCreateWithGeneratedKey(template, sql, entities, parameters, keyColumn);
    }


    public static <ID, A extends Entity<ID>> void batchCreateWithGeneratedKey(NamedParameterJdbcTemplate template,
                                                                            String sql,
                                                                            Collection<A> entities,
                                                                            SqlParameterSource[] parameters) {
        batchCreateWithGeneratedKey(template, sql, entities, parameters, "id");
    }

    public static <ID, A extends Entity<ID>> void batchCreateWithGeneratedKey(NamedParameterJdbcTemplate template,
                                                                            String sql,
                                                                            Collection<A> entities,
                                                                            SqlParameterSource[] parameters,
                                                                            String keyColumn) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        template.batchUpdate(sql, parameters, keyHolder, new String[] {keyColumn});
        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        Assert.isTrue(keyHolder.getKeyList().size() == entities.size(), "Generated ID count doesn't match the expected!");
        Iterator<A> entityIterator = entities.iterator();
        for (Map<String, Object> map : keyList) {
            // Since only one column is returned, we take the first value as the generated key.
            // Note: Mysql returns "GENERATED_KEY" as the key in the map rather than keyColumn.
            Object key = null;
            Iterator<Object> iterator = map.values().iterator();
            if (iterator.hasNext()) {
                key = iterator.next();
            }
            if (key instanceof Number keyValue) {
                assignId(entityIterator.next(), keyValue);
            } else {
                throw new DataRetrievalFailureException("Generated Key is not valid or not found!");
            }
        }
    }

}
