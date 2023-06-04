package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    @NotNull
    private String name;
    @Email(message = "e-mail невалидный")
    @NotNull(message = "необходим e-mail")
    @NotBlank
    private String email;
}
