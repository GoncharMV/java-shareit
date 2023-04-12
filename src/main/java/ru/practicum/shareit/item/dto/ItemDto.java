package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Setter
@Getter
@AllArgsConstructor
public class ItemDto {
    private String name;
    private String description;
    private boolean available;
    private Long requestId;

}
