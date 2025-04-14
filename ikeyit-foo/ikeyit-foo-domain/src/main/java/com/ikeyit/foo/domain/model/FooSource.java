package com.ikeyit.foo.domain.model;

/**
 * A value object to define the source of the foo message
 * @param device
 * @param port
 */
public record FooSource(String device, String port){
}
