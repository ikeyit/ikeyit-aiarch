package com.ikeyit.common.web.exception;


import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Global error controller. When filter/internal errors occur, the controller will handle these error.
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class RestErrorController extends AbstractErrorController {
    private static final Logger log = LoggerFactory.getLogger(RestErrorController.class);

    private static final String MESSAGE_KEY_PREFIX = "error.";
    private final MessageSource messageSource;

    public RestErrorController(ErrorAttributes errorAttributes, MessageSource messageSource) {
        super(errorAttributes);
        this.messageSource = messageSource;
    }

    // HTML request
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
                .including(ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE);
        Map<String, Object> attributes = getErrorAttributes(request, options);
//        String path = (String) attributes.get("path");
        String message = (String) attributes.get("message");
        response.setStatus(status.value());
       return ResponseEntity.status(status)
               .contentType(MediaType.TEXT_HTML)
               .body(message);
    }

    @RequestMapping
    public ResponseEntity<ErrorResp> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
                .including(ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE);
        Map<String, Object> attributes = getErrorAttributes(request, options);
        String path = (String) attributes.get("path");
        String message = (String) attributes.get("message");
        if (HttpStatus.NOT_FOUND == status) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorRespBuilder
                            .of(CommonErrorCode.NOT_FOUND)
                            .setMessageSource(messageSource)
                            .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                            .setMessageKey("notFound")
                            .setDefaultMessage("Path {0} is not found!")
                            .setArgs(path)
                            .build());
        } else if (HttpStatus.FORBIDDEN == status) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorRespBuilder
                            .of(CommonErrorCode.FORBIDDEN)
                            .setMessageSource(messageSource)
                            .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                            .setMessageKey("forbidden")
                            .setArgs(message)
                            .setDefaultMessage("{0}")
                            .build());
        } else if (HttpStatus.UNAUTHORIZED == status) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorRespBuilder
                            .of(CommonErrorCode.UNAUTHORIZED)
                            .setMessageSource(messageSource)
                            .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                            .setMessageKey("unauthorized")
                            .setArgs(message)
                            .setDefaultMessage("{0}")
                            .build());
        }

        log.error("Error: {}", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.INTERNAL_SERVER_ERROR)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("internalServerError")
                        .setArgs(message)
                        .setDefaultMessage("{0}")
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResp> mediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.BAD_REQUEST)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("notAcceptable")
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }


}