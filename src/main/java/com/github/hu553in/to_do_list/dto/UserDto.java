package com.github.hu553in.to_do_list.dto;

import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.model.UserDetails;
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
        this.username = String.valueOf(authentication.getPrincipal());
        if (!(authentication.getDetails() instanceof UserDetails userDetails)) {
            throw new IllegalArgumentException("Authentication details must be of type "
                    + UserDetails.class.getCanonicalName());
        }
        this.id = userDetails.id();
        this.isAdmin = authentication
                .getAuthorities()
                .stream()
                .anyMatch(it -> Objects.equals(it.getAuthority(), Authority.ROLE_ADMIN.toString()));
    }

}
