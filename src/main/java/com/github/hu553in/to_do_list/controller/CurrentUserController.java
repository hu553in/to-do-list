package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.exception.ServerErrorException;
import com.github.hu553in.to_do_list.service.ICurrentUserService;
import com.github.hu553in.to_do_list.service.IUserService;
import com.github.hu553in.to_do_list.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class CurrentUserController {

    private final ICurrentUserService currentUserService;
    private final IUserService userService;
    private final ConversionService conversionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserView me() {
        Integer currentUserId = currentUserService.getCurrentUser().id();
        try {
            return conversionService.convert(userService.getById(currentUserId), UserView.class);
        } catch (NotFoundException e) {
            throw new ServerErrorException("Current user is not found by ID");
        }
    }

}
