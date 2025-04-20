package com.ikeyit.common.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class providing assertion methods for business logic validation.
 * Each assertion method throws a BizException when the condition is not met.
 * Methods are overloaded to support different combinations of error codes, message keys, and message arguments.
 */
public class BizAssert {

    /**
     * Asserts that the given object is not null.
     * @param obj The object to check
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the object is null
     */
    public static void notNull(@Nullable Object obj, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (obj == null) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given object is not null.
     * @param obj The object to check
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the object is null
     */
    public static void notNull(@Nullable Object obj, ErrorCode errorCode, String message, Object... args) {
        if (obj == null) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that the given object is not null.
     * @param obj The object to check
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the object is null with INVALID_ARGUMENT error code
     */
    public static void notNull(@Nullable Object obj, MessageKey messageKey, String message, Object... args) {
        if (obj == null) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given object is not null.
     * @param obj The object to check
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the object is null with INVALID_ARGUMENT error code
     */
    public static void notNull(@Nullable Object obj, String message, Object... args) {
        if (obj == null) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is true.
     * @param expression The boolean expression to check
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is false
     */
    public static void isTrue(boolean expression, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (!expression) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is true.
     * @param expression The boolean expression to check
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is false
     */
    public static void isTrue(boolean expression, ErrorCode errorCode, String message, Object... args) {
        if (!expression) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is true.
     * @param expression The boolean expression to check
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is false with INVALID_ARGUMENT error code
     */
    public static void isTrue(boolean expression, MessageKey messageKey, String message, Object... args) {
        if (!expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is true.
     * @param expression The boolean expression to check
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is false with INVALID_ARGUMENT error code
     */
    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is false.
     * @param expression The boolean expression to check
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is true
     */
    public static void isFalse(boolean expression, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (expression) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is false.
     * @param expression The boolean expression to check
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is true
     */
    public static void isFalse(boolean expression, ErrorCode errorCode, String message, Object... args) {
        if (expression) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is false.
     * @param expression The boolean expression to check
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is true with INVALID_ARGUMENT error code
     */
    public static void isFalse(boolean expression, MessageKey messageKey, String message, Object... args) {
        if (expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given boolean expression is false.
     * @param expression The boolean expression to check
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the expression is true with INVALID_ARGUMENT error code
     */
    public static void isFalse(boolean expression, String message, Object... args) {
        if (expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that the given string has length (is not null and not empty).
     * @param str The string to check
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the string is null or empty
     */
    public static void notEmpty(@Nullable String str, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given string has length (is not null and not empty).
     * @param str The string to check
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the string is null or empty
     */
    public static void notEmpty(@Nullable String str, ErrorCode errorCode, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that the given string has length (is not null and not empty).
     * @param str The string to check
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the string is null or empty with INVALID_ARGUMENT error code
     */
    public static void notEmpty(@Nullable String str, MessageKey messageKey, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that the given string has length (is not null and not empty).
     * @param str The string to check
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if the string is null or empty with INVALID_ARGUMENT error code
     */
    public static void notEmpty(@Nullable String str, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that two objects are equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are not equal
     */
    public static void equals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that two objects are equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are not equal
     */
    public static void equals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that two objects are equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are not equal with INVALID_ARGUMENT error code
     */
    public static void equals(@Nullable Object expected, @Nullable Object actual, MessageKey messageKey, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that two objects are equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are not equal with INVALID_ARGUMENT error code
     */
    public static void equals(@Nullable Object expected, @Nullable Object actual, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that two objects are not equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are equal
     */
    public static void notEquals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that two objects are not equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are equal
     */
    public static void notEquals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that two objects are not equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are equal with INVALID_ARGUMENT error code
     */
    public static void notEquals(@Nullable Object expected, @Nullable Object actual, MessageKey messageKey, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that two objects are not equal.
     * @param expected The expected object
     * @param actual The actual object
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if objects are equal with INVALID_ARGUMENT error code
     */
    public static void notEquals(@Nullable Object expected, @Nullable Object actual, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that a collection is not empty.
     * @param collection The collection to check
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if collection is null or empty
     */
    public static void notEmpty(@Nullable Collection<?> collection, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that a collection is not empty.
     * @param collection The collection to check
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if collection is null or empty
     */
    public static void notEmpty(@Nullable Collection<?> collection, ErrorCode errorCode, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that a collection is not empty.
     * @param collection The collection to check
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if collection is null or empty with INVALID_ARGUMENT error code
     */
    public static void notEmpty(@Nullable Collection<?> collection,MessageKey messageKey, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that a collection is not empty.
     * @param collection The collection to check
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if collection is null or empty with INVALID_ARGUMENT error code
     */
    public static void notEmpty(@Nullable Collection<?> collection, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that an array is not empty.
     * @param array The array to check
     * @param errorCode The error code to use if assertion fails
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if array is null or empty
     */
    public static void notEmpty(@Nullable Object[] array, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    /**
     * Asserts that an array is not empty.
     * @param array The array to check
     * @param errorCode The error code to use if assertion fails
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if array is null or empty
     */
    public static void notEmpty(@Nullable Object[] array, ErrorCode errorCode, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(errorCode, message, args);
        }
    }

    /**
     * Asserts that an array is not empty.
     * @param array The array to check
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if array is null or empty with INVALID_ARGUMENT error code
     */
    public static void notEmpty(@Nullable Object[] array, MessageKey messageKey, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    /**
     * Asserts that an array is not empty.
     * @param array The array to check
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if array is null or empty with INVALID_ARGUMENT error code
     */
    public static void notEmpty(@Nullable Object[] array, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    /**
     * Asserts that a collection has no duplicate values after applying the mapper function.
     * @param <T> The type of elements in the collection
     * @param <R> The type of mapped values to check for duplicates
     * @param collection The collection to check
     * @param mapper The function to map elements to values for duplicate checking
     * @param message The error message format
     * @param args The message format arguments
     * @throws BizException if any duplicates are found with INVALID_ARGUMENT error code
     */
    public static <T, R> void notDuplicated(@Nullable Collection<T> collection, @Nonnull Function<? super T, R> mapper, String message, Object... args) {
        if (collection == null || collection.size() <= 1) {
            return;
        }
        Set<R> map = new HashSet<>(collection.size());
        for (T value : collection) {
            R mappedValue = mapper.apply(value);
            if (map.contains(mappedValue)) {
                throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
            } else {
                map.add(mappedValue);
            }
        }
    }

    /**
     * Creates a supplier that throws a BizException with the given parameters.
     * @param errorCode The error code to use
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @return A supplier that throws BizException
     */
    public static Supplier<BizException> failSupplier(ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        return () -> new BizException(errorCode, messageKey, message, args);
    }

    /**
     * Creates a supplier that throws a BizException with the given parameters.
     * @param errorCode The error code to use
     * @param message The error message format
     * @param args The message format arguments
     * @return A supplier that throws BizException
     */
    public static Supplier<BizException> failSupplier(ErrorCode errorCode, String message, Object... args) {
        return () -> new BizException(errorCode, message, args);
    }

    /**
     * Creates a supplier that throws a BizException with INVALID_ARGUMENT error code.
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @return A supplier that throws BizException
     */
    public static Supplier<BizException> failSupplier(MessageKey messageKey, String message, Object... args) {
        return () -> new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
    }

    /**
     * Creates a supplier that throws a BizException with INVALID_ARGUMENT error code.
     * @param message The error message format
     * @param args The message format arguments
     * @return A supplier that throws BizException
     */
    public static Supplier<BizException> failSupplier(String message, Object... args) {
        return () -> new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
    }

    /**
     * Creates a consumer that throws a BizException with the given parameters.
     * @param <T> The type of the consumer parameter
     * @param errorCode The error code to use
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @return A consumer that throws BizException
     */
    public static <T> Consumer<T> failAction(ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        return a -> {throw new BizException(errorCode, messageKey, message, args);};
    }

    /**
     * Creates a consumer that throws a BizException with the given parameters.
     * @param <T> The type of the consumer parameter
     * @param errorCode The error code to use
     * @param message The error message format
     * @param args The message format arguments
     * @return A consumer that throws BizException
     */
    public static <T> Consumer<T> failAction(ErrorCode errorCode, String message, Object... args) {
        return a -> {throw new BizException(errorCode, message, args);};
    }

    /**
     * Creates a consumer that throws a BizException with INVALID_ARGUMENT error code.
     * @param <T> The type of the consumer parameter
     * @param messageKey The message key for i18n
     * @param message The error message format
     * @param args The message format arguments
     * @return A consumer that throws BizException
     */
    public static <T> Consumer<T> failAction(MessageKey messageKey, String message, Object... args) {
        return a -> {throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);};
    }

    /**
     * Creates a consumer that throws a BizException with INVALID_ARGUMENT error code.
     * @param <T> The type of the consumer parameter
     * @param message The error message format
     * @param args The message format arguments
     * @return A consumer that throws BizException
     */
    public static <T> Consumer<T> failAction(String message, Object... args) {
        return a -> {throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);};
    }
}
