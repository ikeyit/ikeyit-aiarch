package com.ikeyit.common.data;

import java.util.Locale;
import java.util.StringJoiner;

public class SortParam {
    private String sortKey;
    private Direction sortDirection;

    public SortParam() {
    }


    public SortParam(String sortKey, Direction sortDirection) {
        this.sortKey = sortKey;
        this.sortDirection = sortDirection;
    }

    public void setSortKey(String sortKey) {
        if (sortKey != null)
            sortKey = sortKey.trim();
        this.sortKey = sortKey == null || sortKey.isEmpty() ? null : sortKey.trim();
    }

    public void setSortDirection(Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setSort(String sort) {
        if (sort != null) {
            if (sort.endsWith("-")) {
                sortDirection = Direction.DESC;
            } else if (sort.endsWith("+")) {
                sortDirection = Direction.ASC;
            }
            sortKey = sort.substring(0, sort.length() - (sortDirection == null ? 0 : 1));
        }
    }

    public String getSortKey() {
        return sortKey;
    }

    public Direction getSortDirection() {
        return sortDirection;
    }

    public String getSortKeyOrDefault(String defaultValue) {
        if (defaultValue == null)
            throw new IllegalArgumentException("Default value is null");
        return sortKey == null || sortKey.isEmpty() ? defaultValue : sortKey;
    }

    public Direction getSortDirectionOrDefault(Direction defaultValue) {
        if (defaultValue == null)
            throw new IllegalArgumentException("Default value is null");
        return sortDirection == null ? defaultValue: sortDirection;
    }

    public static SortParam of(String sort) {
        SortParam sortParam = new SortParam();
        sortParam.setSort(sort);
        return sortParam;
    }

    public enum Direction {
        ASC, DESC;
        public static Direction of(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given; Has to be either 'desc' or 'asc' (case insensitive)", value), e);
            }
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SortParam.class.getSimpleName() + "[", "]")
                .add("sortKey='" + sortKey + "'")
                .add("sortDirection=" + sortDirection)
                .toString();
    }
}
