package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {

    private Long id;
    @NotNull(message = "Необходимо описание запроса")
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();
}
