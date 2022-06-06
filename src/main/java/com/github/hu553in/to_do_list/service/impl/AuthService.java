package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.exception.UnauthorizedException;
import com.github.hu553in.to_do_list.exception.UsernameTakenException;
import com.github.hu553in.to_do_list.form.SignInForm;
import com.github.hu553in.to_do_list.form.SignUpForm;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;

    @Override
    public UserDto signIn(final SignInForm form) {
        UserEntity user = userRepository
                .findByUsername(form.username())
                .orElseThrow(NotFoundException::new);
        if (!passwordEncoder.matches(form.password(), user.getPassword())) {
            throw new UnauthorizedException();
        }
        return conversionService.convert(user, UserDto.class);
    }

    @Override
    public void signUp(final SignUpForm form) {
        String username = form.username();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameTakenException();
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(form.password()));
        userRepository.saveAndFlush(user);
    }

}
