package com.ikeyit.common.exception;

import javax.annotation.Nonnull;

public interface MessageKey {
    @Nonnull
    String value();

    static MessageKey of(@Nonnull String value) {
        return new DefaultMessageKey(value);
    }

    record DefaultMessageKey(String value) implements MessageKey {

        public DefaultMessageKey(@Nonnull String value) {
            if (value == null) {
                throw new IllegalArgumentException("Value is null!");
            }
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }


}
