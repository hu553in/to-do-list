package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.SignInDto;
import com.github.hu553in.to_do_list.dto.SignUpDto;

public interface IAuthService {

    String signIn(SignInDto dto);

    void signUp(SignUpDto dto);

}
