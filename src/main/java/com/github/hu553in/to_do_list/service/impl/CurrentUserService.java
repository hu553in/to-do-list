package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.exception.ServerErrorException;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.service.ICurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService implements ICurrentUserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Integer currentUserId)) {
            throw new ServerErrorException("Authentication principal must be integer representing current user ID");
        }
        return userRepository
                .findById(currentUserId)
                .map(it -> conversionService.convert(it, UserDto.class))
                .orElseThrow(() -> new ServerErrorException("Current user is not found by ID"));
    }

}
