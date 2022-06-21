package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.exception.AuthorizationFailedException;
import com.github.hu553in.to_do_list.exception.EmailTakenException;
import com.github.hu553in.to_do_list.form.SignInForm;
import com.github.hu553in.to_do_list.form.SignUpForm;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.service.IAuthService;
import com.github.hu553in.to_do_list.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;

    @Override
    public String signIn(final SignInForm form) {
        UserEntity user = userRepository
                .findByEmail(form.email())
                .filter(it -> passwordEncoder.matches(form.password(), it.getPassword()))
                .orElseThrow(AuthorizationFailedException::new);
        return jwtService.buildJwt(conversionService.convert(user, UserDto.class));
    }

    @Override
    @Transactional
    public void signUp(final SignUpForm form) {
        String email = form.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailTakenException();
        }
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(form.password()));
        userRepository.saveAndFlush(user);
    }

}
