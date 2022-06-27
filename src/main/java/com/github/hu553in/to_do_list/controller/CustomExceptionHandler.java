package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.exception.AuthorizationFailedException;
import com.github.hu553in.to_do_list.exception.EmailTakenException;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.exception.ServerErrorException;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        String message = "Some method arguments are not valid";
        logger.error(message, e);
        Collection<String> details = new HashSet<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult
                .getFieldErrors()
                .forEach(it -> details.add(it.getField() + ": " + it.getDefaultMessage()));
        bindingResult
                .getGlobalErrors()
                .forEach(it -> details.add(it.getObjectName() + ": " + it.getDefaultMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(buildApiErrorView(message, details));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleConversionNotSupported(@NonNull final ConversionNotSupportedException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        String message = "Conversion is not supported";
        logger.error(message, e);
        StringBuilder stringBuilder = new StringBuilder();
        Class<?> requiredType = e.getRequiredType();
        stringBuilder
                .append("Conversion of property ")
                .append(e.getPropertyName())
                .append(" with value ")
                .append(e.getValue())
                .append(requiredType != null ? " to " + requiredType.getName() : " to required type")
                .append(" is not supported");
        Collection<String> details = List.of(stringBuilder.toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(buildApiErrorView(message, details));
    }

    @Hidden
    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorView handleConversionFailed(final ConversionFailedException e) {
        String message = "Conversion is failed";
        logger.error(message, e);
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
        Collection<String> details = List.of(stringBuilder.toString());
        return buildApiErrorView(message, details);
    }

    @Hidden
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorView handleConstraintViolation(final ConstraintViolationException e) {
        String message = "Some constraints have been violated";
        logger.error(message, e);
        Collection<String> details = e
                .getConstraintViolations()
                .stream()
                .map(it -> "Root bean " + it.getRootBeanClass().getName()
                           + ", path " + it.getPropertyPath()
                           + ": " + it.getMessage())
                .collect(Collectors.toSet());
        return buildApiErrorView(message, details);
    }

    @Hidden
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorView handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException e) {
        String message = "Some method argument types do not match";
        logger.error(message, e);
        Class<?> requiredType = e.getRequiredType();
        String name = e.getName();
        Collection<String> details = List.of(requiredType != null
                ? name + " must be of type " + requiredType.getName()
                : name + " has invalid type");
        return buildApiErrorView(message, details);
    }

    @Hidden
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorView handleNotFound(final NotFoundException e) {
        String message = "Some entities are not found";
        logger.error(message, e);
        return buildApiErrorView(message);
    }

    @Hidden
    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiErrorView handleEmailTaken(final EmailTakenException e) {
        String message = "Email is already taken";
        logger.error(message, e);
        return buildApiErrorView(message);
    }

    @Hidden
    @ExceptionHandler(AuthorizationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiErrorView handleAuthorizationFailed(final AuthorizationFailedException e) {
        String message = "Authorization is failed";
        logger.error(message, e);
        return buildApiErrorView(message);
    }

    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorView handleServerError(final ServerErrorException e) {
        logger.error(e);
        return buildApiErrorView("Unknown server error");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorView handleAnother(final Exception e) {
        logger.error(e);
        return buildApiErrorView("Unknown error");
    }

    private ApiErrorView buildApiErrorView(final String message, final Collection<String> details) {
        return ApiErrorView
                .builder()
                .message(message)
                .details(details)
                .build();
    }

    private ApiErrorView buildApiErrorView(final String message) {
        return buildApiErrorView(message, List.of());
    }

}
