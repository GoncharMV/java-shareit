package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto item, Long ownerId);

    ItemDto editItem(ItemDto item, Long ownerId, Long itemId);

    ItemDto getItem(Long itemId);

    Collection<ItemDto> getOwnerItems(Long ownerId);

    Collection<ItemDto> search(String text);
}

