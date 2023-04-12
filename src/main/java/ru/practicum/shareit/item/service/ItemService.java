package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add();

    ItemDto edit();

    ItemDto get(Long itemId);

    List<ItemDto> getOwnerItems(Long ownerId);
}

