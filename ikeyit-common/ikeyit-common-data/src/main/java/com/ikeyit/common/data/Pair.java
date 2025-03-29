package com.ikeyit.common.data;

/**
 * Represents a pair of two elements of different types.
 * 
 * @param <A> The type of the first element
 * @param <B> The type of the second element
 */
public record Pair<A, B>(A first, B second) {}