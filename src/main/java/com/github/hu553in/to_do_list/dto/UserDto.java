package com.github.hu553in.to_do_list.dto;

import com.github.hu553in.to_do_list.model.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String username;
    private Boolean isAdmin;

    public UserDto(final Authentication authentication) {
        this.id = Integer.valueOf(String.valueOf(authentication.getPrincipal()));
        this.username = String.valueOf(authentication.getDetails());
        this.isAdmin = authentication
                .getAuthorities()
                .stream()
                .anyMatch(it -> Objects.equals(it.getAuthority(), Authority.ROLE_ADMIN.toString()));
    }

}
