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
 * @author: lz
 * @date: 2021/6/10 13:51
 */
public class BizAssert {

    public static void notNull(@Nullable Object obj, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (obj == null) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void notNull(@Nullable Object obj, ErrorCode errorCode, String message, Object... args) {
        if (obj == null) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void notNull(@Nullable Object obj, MessageKey messageKey, String message, Object... args) {
        if (obj == null) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void notNull(@Nullable Object obj, String message, Object... args) {
        if (obj == null) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void isTrue(boolean expression, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (!expression) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void isTrue(boolean expression, ErrorCode errorCode, String message, Object... args) {
        if (!expression) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void isTrue(boolean expression, MessageKey messageKey, String message, Object... args) {
        if (!expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void isFalse(boolean expression, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (expression) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void isFalse(boolean expression, ErrorCode errorCode, String message, Object... args) {
        if (expression) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void isFalse(boolean expression, MessageKey messageKey, String message, Object... args) {
        if (expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void isFalse(boolean expression, String message, Object... args) {
        if (expression) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void hasLength(@Nullable String str, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void hasLength(@Nullable String str, ErrorCode errorCode, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void hasLength(@Nullable String str, MessageKey messageKey, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void hasLength(@Nullable String str, String message, Object... args) {
        if (str == null || str.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void equals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void equals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void equals(@Nullable Object expected, @Nullable Object actual, MessageKey messageKey, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void equals(@Nullable Object expected, @Nullable Object actual, String message, Object... args) {
        if (!Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void notEquals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void notEquals(@Nullable Object expected, @Nullable Object actual, ErrorCode errorCode, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void notEquals(@Nullable Object expected, @Nullable Object actual, MessageKey messageKey, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void notEquals(@Nullable Object expected, @Nullable Object actual, String message, Object... args) {
        if (Objects.equals(expected, actual)) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, ErrorCode errorCode, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection,MessageKey messageKey, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

    public static void notEmpty(@Nullable Object[] array, ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(errorCode, messageKey, message, args);
        }
    }

    public static void notEmpty(@Nullable Object[] array, ErrorCode errorCode, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(errorCode, message, args);
        }
    }

    public static void notEmpty(@Nullable Object[] array, MessageKey messageKey, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
        }
    }

    public static void notEmpty(@Nullable Object[] array, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
        }
    }

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

    public static Supplier<BizException> failSupplier(ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        return () -> new BizException(errorCode, messageKey, message, args);
    }

    public static Supplier<BizException> failSupplier(ErrorCode errorCode, String message, Object... args) {
        return () -> new BizException(errorCode, message, args);
    }

    public static Supplier<BizException> failSupplier(MessageKey messageKey, String message, Object... args) {
        return () -> new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);
    }

    public static Supplier<BizException> failSupplier(String message, Object... args) {
        return () -> new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);
    }

    public static <T> Consumer<T> failAction(ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        return a -> {throw new BizException(errorCode, messageKey, message, args);};
    }

    public static <T> Consumer<T> failAction(ErrorCode errorCode, String message, Object... args) {
        return a -> {throw new BizException(errorCode, message, args);};
    }

    public static <T> Consumer<T> failAction(MessageKey messageKey, String message, Object... args) {
        return a -> {throw new BizException(CommonErrorCode.INVALID_ARGUMENT, messageKey, message, args);};
    }

    public static <T> Consumer<T> failAction(String message, Object... args) {
        return a -> {throw new BizException(CommonErrorCode.INVALID_ARGUMENT, message, args);};
    }
}
