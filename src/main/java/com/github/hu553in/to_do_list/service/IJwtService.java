package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface IJwtService {

    String buildToken(UserDto user);

    Authentication authenticateToken(String token);

}
