package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface IJwtService {

    String buildJwt(UserDto user);

    Authentication authenticateJwt(String jwt);

}
