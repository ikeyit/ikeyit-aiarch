package com.ikeyit.common.data;

import java.util.Locale;
import java.util.StringJoiner;

/**
 * A class representing sorting parameters for data queries.
 * Supports specifying a sort key and direction (ascending or descending).
 * Also provides parsing functionality for sort strings in the format "field+" or "field-".
 */
public class SortParam {
    private String sortKey;
    private Direction sortDirection;

    /**
     * Creates a new SortParam instance with default values.
     */
    public SortParam() {
    }

    /**
     * Creates a new SortParam instance with specified sort key and direction.
     *
     * @param sortKey the field name to sort by
     * @param sortDirection the direction to sort in
     */
    public SortParam(String sortKey, Direction sortDirection) {
        this.sortKey = sortKey;
        this.sortDirection = sortDirection;
    }

    /**
     * Sets the sort key after trimming whitespace.
     * Null or empty strings are converted to null.
     *
     * @param sortKey the field name to sort by
     */
    public void setSortKey(String sortKey) {
        if (sortKey != null)
            sortKey = sortKey.trim();
        this.sortKey = sortKey == null || sortKey.isEmpty() ? null : sortKey.trim();
    }

    /**
     * Sets the sort direction.
     *
     * @param sortDirection the direction to sort in
     */
    public void setSortDirection(Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * Parses a sort string in the format "field+" or "field-" and sets both key and direction.
     * A "+" suffix indicates ascending order, while "-" indicates descending order.
     *
     * @param sort the sort string to parse
     */
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

    /**
     * Returns the sort key.
     *
     * @return the field name to sort by
     */
    public String getSortKey() {
        return sortKey;
    }

    /**
     * Returns the sort direction.
     *
     * @return the direction to sort in
     */
    public Direction getSortDirection() {
        return sortDirection;
    }

    /**
     * Returns the sort key if set, otherwise returns the provided default value.
     *
     * @param defaultValue the default value to return if sort key is not set
     * @return the sort key or default value
     * @throws IllegalArgumentException if defaultValue is null
     */
    public String getSortKeyOrDefault(String defaultValue) {
        if (defaultValue == null)
            throw new IllegalArgumentException("Default value is null");
        return sortKey == null || sortKey.isEmpty() ? defaultValue : sortKey;
    }

    /**
     * Returns the sort direction if set, otherwise returns the provided default value.
     *
     * @param defaultValue the default value to return if sort direction is not set
     * @return the sort direction or default value
     * @throws IllegalArgumentException if defaultValue is null
     */
    public Direction getSortDirectionOrDefault(Direction defaultValue) {
        if (defaultValue == null)
            throw new IllegalArgumentException("Default value is null");
        return sortDirection == null ? defaultValue: sortDirection;
    }

    /**
     * Creates a new SortParam instance from a sort string.
     *
     * @param sort the sort string to parse
     * @return a new SortParam instance
     */
    public static SortParam of(String sort) {
        SortParam sortParam = new SortParam();
        sortParam.setSort(sort);
        return sortParam;
    }

    /**
     * Enumeration of possible sort directions.
     */
    public enum Direction {
        /** Ascending order */
        ASC,
        /** Descending order */
        DESC;

        /**
         * Creates a Direction from a string value.
         *
         * @param value the string value to parse ("asc" or "desc", case insensitive)
         * @return the corresponding Direction
         * @throws IllegalArgumentException if the value is not valid
         */
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
