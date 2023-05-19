package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
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
