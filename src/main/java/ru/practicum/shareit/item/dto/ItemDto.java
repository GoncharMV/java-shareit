package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull(message = "Необходимо название предмета")
    @NotEmpty(message = "Необходимо название предмета")
    private String name;
    @NotNull(message = "Необходимо описание предмета")
    private String description;
    @NotNull
    private Boolean available;

}
