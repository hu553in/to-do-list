package com.github.hu553in.to_do_list.dto;

public record UpdateUserDto(String email, String password, Boolean admin) {
}
