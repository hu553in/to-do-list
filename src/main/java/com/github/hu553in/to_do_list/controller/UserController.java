package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.dto.UpdateUserDto;
import com.github.hu553in.to_do_list.form.UpdateUserForm;
import com.github.hu553in.to_do_list.service.IUserService;
import com.github.hu553in.to_do_list.swagger.BearerJwtAuthSecurityScheme;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import com.github.hu553in.to_do_list.view.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Tag(name = "User", description = "The user API")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final ConversionService conversionService;

    @Operation(
        summary = "[ADMIN] Get all users",
        security = @SecurityRequirement(name = BearerJwtAuthSecurityScheme.NAME))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<UserView> getAll() {
        return userService
            .getAll()
            .stream()
            .map(it -> conversionService.convert(it, UserView.class))
            .collect(Collectors.toSet());
    }

    @Operation(
        summary = "[ADMIN] Get the user by ID",
        security = @SecurityRequirement(name = BearerJwtAuthSecurityScheme.NAME),
        responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(implementation = ApiErrorView.class))),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(implementation = ApiErrorView.class)))
        })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserView getById(@PathVariable("id") final Integer id) {
        return conversionService.convert(userService.getById(id), UserView.class);
    }

    @Operation(
        summary = "[ADMIN] Update the user",
        security = @SecurityRequirement(name = BearerJwtAuthSecurityScheme.NAME),
        responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(implementation = ApiErrorView.class))),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(implementation = ApiErrorView.class))),
            @ApiResponse(
                responseCode = "409",
                description = "Email is already taken",
                content = @Content(schema = @Schema(implementation = ApiErrorView.class)))
        })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") final Integer id, @Valid @RequestBody final UpdateUserForm form) {
        userService.updateById(id, conversionService.convert(form, UpdateUserDto.class));
    }

}
