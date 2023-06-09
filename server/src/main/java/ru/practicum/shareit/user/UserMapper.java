package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .userId(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
