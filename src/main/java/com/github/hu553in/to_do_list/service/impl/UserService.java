package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.exception.UsernameTakenException;
import com.github.hu553in.to_do_list.form.UpdateUserForm;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(it -> conversionService.convert(it, UserDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto findById(final Integer id) {
        return userRepository
                .findById(id)
                .map(it -> conversionService.convert(it, UserDto.class))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public void update(final Integer id, final UpdateUserForm form) {
        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        Optional
                .ofNullable(form.username())
                .ifPresent(value -> {
                    if (userRepository.findByUsername(value).isPresent()) {
                        throw new UsernameTakenException();
                    }
                    user.setUsername(value);
                });
        Optional
                .ofNullable(form.password())
                .ifPresent(value -> user.setPassword(passwordEncoder.encode(value)));
        Optional
                .ofNullable(form.isAdmin())
                .ifPresent(user::setIsAdmin);
        userRepository.saveAndFlush(user);
    }

}
