package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String name;
    private String email;
}
