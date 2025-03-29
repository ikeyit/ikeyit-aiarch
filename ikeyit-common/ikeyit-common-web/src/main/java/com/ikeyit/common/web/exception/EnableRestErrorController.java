package com.ikeyit.common.web.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RestErrorController.class)
public @interface EnableRestErrorController {

}