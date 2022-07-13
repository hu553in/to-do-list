package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.CurrentUserDto;
import com.github.hu553in.to_do_list.enumeration.Authority;
import com.github.hu553in.to_do_list.exception.ServerErrorException;
import com.github.hu553in.to_do_list.service.ICurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrentUserService implements ICurrentUserService {

    @Override
    public CurrentUserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Integer currentUserId)) {
            throw new ServerErrorException("Authentication principal must be integer representing current user ID");
        }
        Boolean admin = authentication
            .getAuthorities()
            .stream()
            .anyMatch(it -> Objects.equals(it.getAuthority(), Authority.ROLE_ADMIN.toString()));
        return new CurrentUserDto(currentUserId, admin);
    }

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Integer currentUserId)) {
            return null;
        }
        return currentUserId;
    }

}
