package com.ikeyit.common.web.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * Enables REST error handling capabilities in a Spring application.
 * When this annotation is applied to a configuration class, it imports and configures
 * the RestErrorController, which provides centralized error handling for REST endpoints.
 * 
 * This controller handles various error scenarios including:
 * - 404 Not Found errors
 * - 403 Forbidden errors
 * - 401 Unauthorized errors
 * - 500 Internal Server errors
 * 
 * The controller provides both HTML and JSON responses based on the request's Accept header,
 * making it suitable for both browser and API clients.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RestErrorController.class)
public @interface EnableRestErrorController {

}