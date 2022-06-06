package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.exception.UsernameTakenException;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        logger.error(e);
        List<String> details = e
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(buildApiErrorView(HttpStatus.BAD_REQUEST,
                        "Some method arguments are not valid",
                        details));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleConversionNotSupported(@NonNull final ConversionNotSupportedException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        logger.error(e);
        StringBuilder stringBuilder = new StringBuilder();
        Class<?> requiredType = e.getRequiredType();
        stringBuilder
                .append("Conversion of property ")
                .append(e.getPropertyName())
                .append(" with value ")
                .append(e.getValue())
                .append(requiredType != null ? " to " + requiredType.getName() : " to required type")
                .append(" is not supported");
        List<String> details = List.of(stringBuilder.toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(buildApiErrorView(HttpStatus.BAD_REQUEST, "Conversion is not supported", details));
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorView handleConversionFailed(final ConversionFailedException e) {
        logger.error(e);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Unable to convert value ")
                .append(e.getValue());
        TypeDescriptor sourceType = e.getSourceType();
        if (sourceType != null) {
            stringBuilder
                    .append(" from ")
                    .append(sourceType.getName());
        }
        stringBuilder
                .append(" to ")
                .append(e.getTargetType().getName());
        List<String> details = List.of(stringBuilder.toString());
        return buildApiErrorView(HttpStatus.BAD_REQUEST, "Conversion is failed", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorView handleConstraintViolation(final ConstraintViolationException e) {
        logger.error(e);
        List<String> details = e
                .getConstraintViolations()
                .stream()
                .map(cv -> "Root bean " + cv.getRootBeanClass().getName()
                        + ", path " + cv.getPropertyPath()
                        + ": " + cv.getMessage())
                .collect(Collectors.toList());
        return buildApiErrorView(HttpStatus.BAD_REQUEST, "Some constraints have been violated", details);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorView handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException e) {
        logger.error(e);
        Class<?> requiredType = e.getRequiredType();
        String name = e.getName();
        List<String> details = List.of(requiredType != null
                ? name + " should be of type " + requiredType.getName()
                : name + " has invalid type");
        return buildApiErrorView(HttpStatus.BAD_REQUEST, "Some method argument types do not match", details);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorView handleNotFound(final NotFoundException e) {
        logger.error(e);
        return buildApiErrorView(HttpStatus.NOT_FOUND, "Some entities are not found");
    }

    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiErrorView handleUsernameTaken(final UsernameTakenException e) {
        logger.error(e);
        return buildApiErrorView(HttpStatus.CONFLICT, "Username is already taken");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorView handleAnother(final Exception e) {
        logger.error(e);
        return buildApiErrorView(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error");
    }

    private ApiErrorView buildApiErrorView(final HttpStatus status,
                                           final String message,
                                           final List<String> details) {
        return ApiErrorView
                .builder()
                .status(status)
                .message(message)
                .details(details)
                .build();
    }

    private ApiErrorView buildApiErrorView(final HttpStatus status, final String message) {
        return buildApiErrorView(status, message, List.of());
    }

}
