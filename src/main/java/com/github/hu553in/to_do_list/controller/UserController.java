package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.form.UpdateUserForm;
import com.github.hu553in.to_do_list.service.IUserService;
import com.github.hu553in.to_do_list.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final ConversionService conversionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<UserView> getAll() {
        return userService
                .findAll()
                .stream()
                .map(it -> conversionService.convert(it, UserView.class))
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserView getById(@PathVariable("id") final Integer id) {
        return conversionService.convert(userService.findById(id), UserView.class);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") final Integer id, @Valid @RequestBody final UpdateUserForm form) {
        userService.update(id, form);
    }

}
