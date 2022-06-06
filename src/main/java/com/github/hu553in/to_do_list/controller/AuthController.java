package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.form.SignInForm;
import com.github.hu553in.to_do_list.form.SignUpForm;
import com.github.hu553in.to_do_list.service.IAuthService;
import com.github.hu553in.to_do_list.service.IJwtService;
import com.github.hu553in.to_do_list.view.SignInView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IJwtService jwtService;

    @PostMapping(value = "/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SignInView signIn(@Valid @RequestBody final SignInForm form) {
        return new SignInView(jwtService.createToken(authService.signIn(form)));
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@Valid @RequestBody final SignUpForm form) {
        authService.signUp(form);
    }

}
