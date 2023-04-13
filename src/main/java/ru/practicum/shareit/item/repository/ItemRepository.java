package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item add(Item item, Long ownerId);
    Item edit(Item newItem, Long itemId);
    Item getItem(Long id);
    Collection<Item> getOwnerItems(Long ownerId);
    Collection<Item> search(String text);
}
