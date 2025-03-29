package com.ikeyit.common.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {
    public enum Status implements EnumWithInt {

        DRAFT(1),

        ACTIVE(2),

        INACTIVE(3),
        ;

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }

    static class Fat {
        Status status;

        public Fat() {
        }

        public Fat(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Fat fat = (Fat) o;
            return status == fat.status;
        }
    }

    @Test
    void testWithValueEnumToJsonInt() {
        Assertions.assertEquals("2", JsonUtils.writeValueAsString(Status.ACTIVE));
    }

    @Test
    void testJsonToWithValueEnumInt() {
        Assertions.assertEquals(Status.INACTIVE, JsonUtils.readValue("3", Status.class));
    }

    @Test
    void testWithValueEnumToJsonComplex() {
        Assertions.assertEquals("{\"status\":2}", JsonUtils.writeValueAsString(new Fat(Status.ACTIVE)));
    }

    @Test
    void testJsonToWithValueEnumComplex() {
        Assertions.assertEquals(new Fat(Status.INACTIVE), JsonUtils.readValue("{\"status\": 3}", Fat.class));;
    }
}