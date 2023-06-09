package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto item, Long ownerId);

    ItemDto editItem(ItemDto item, Long ownerId, Long itemId);

    ItemDto getItem(Long itemId, Long userId);

    Collection<ItemDto> getOwnerItems(Long ownerId, int from, int size);

    Collection<ItemDto> search(String text, int from, int size);

    CommentDto postComment(Long userId, CommentDto comment, Long itemId);
}

