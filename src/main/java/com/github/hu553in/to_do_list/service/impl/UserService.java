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

import java.util.Collection;
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
    public void update(final Integer id, final UpdateUserForm form) {
        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        boolean updated = false;
        String username = form.username();
        if (username != null) {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new UsernameTakenException();
            }
            user.setUsername(username);
            updated = true;
        }
        String password = form.password();
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
            updated = true;
        }
        Boolean isAdmin = form.isAdmin();
        if (isAdmin != null) {
            user.setIsAdmin(isAdmin);
            updated = true;
        }
        if (updated) {
            userRepository.saveAndFlush(user);
        }
    }

}
