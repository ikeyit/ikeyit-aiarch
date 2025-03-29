package com.ikeyit.common.exception;

import javax.annotation.Nonnull;

/**
 * Interface for message keys used in internationalization.
 * Message keys are used to look up localized messages in resource bundles.
 */
public interface MessageKey {
    /**
     * Returns the string value of this message key.
     * @return The message key value, never null
     */
    @Nonnull
    String value();

    /**
     * Creates a new MessageKey with the specified value.
     * @param value The message key value, must not be null
     * @return A new MessageKey instance
     */
    static MessageKey of(@Nonnull String value) {
        return new DefaultMessageKey(value);
    }

    /**
     * Default implementation of MessageKey interface.
     * @param value The message key value
     */
    record DefaultMessageKey(String value) implements MessageKey {

        /**
         * Creates a new DefaultMessageKey with the specified value.
         * @param value The message key value, must not be null
         * @throws IllegalArgumentException if value is null
         */
        public DefaultMessageKey(@Nonnull String value) {
            if (value == null) {
                throw new IllegalArgumentException("Value is null!");
            }
            this.value = value;
        }

        /**
         * Returns the string value of this message key.
         * @return The message key value
         */
        public String value() {
            return this.value;
        }

        /**
         * Returns the string representation of this message key.
         * @return The message key value
         */
        @Override
        public String toString() {
            return this.value;
        }
    }
}
