package com.ikeyit.common.exception;

public class ArbitraryErrorCode implements ErrorCode{
    private final int value;

    public ArbitraryErrorCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
