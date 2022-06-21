package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.form.SignInForm;
import com.github.hu553in.to_do_list.form.SignUpForm;
import com.github.hu553in.to_do_list.service.IAuthService;
import com.github.hu553in.to_do_list.view.JwtView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Auth", description = "The auth API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @Operation(summary = "Sign in")
    @PostMapping(value = "/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JwtView signIn(@Valid @RequestBody final SignInForm form) {
        return new JwtView(authService.signIn(form));
    }

    @Operation(summary = "Sign up")
    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@Valid @RequestBody final SignUpForm form) {
        authService.signUp(form);
    }

}
