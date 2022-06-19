package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.form.SignInForm;
import com.github.hu553in.to_do_list.form.SignUpForm;

public interface IAuthService {

    String signIn(SignInForm signInForm);

    void signUp(SignUpForm signUpForm);

}
