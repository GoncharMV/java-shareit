package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private long generateId = 1;

    @Override
    public Item add(Item item, Long ownerId) {
        item.setId(generateId++);
        item.setOwner(ownerId);
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item edit(Item newItem, Long itemId) {
        if (itemStorage.containsKey(itemId)) {
            if (newItem.getName() != null) {
                itemStorage.get(itemId).setName(newItem.getName());
            }
            if (newItem.getDescription() != null) {
                itemStorage.get(itemId).setDescription(newItem.getDescription());
            }
            if (newItem.getAvailable() != null) {
                itemStorage.get(itemId).setAvailable(newItem.getAvailable());
            }
        } else {
            throw new ObjectNotFoundException("Пользователя несуществует");
        }
        return getItem(itemId);
    }

    @Override
    public Item getItem(Long id) {
        return itemStorage.get(id);
    }


    @Override
    public Collection<Item> getOwnerItems(Long ownerId) {
        Collection<Item> ownerItems = new ArrayList<>();
        Collection<Item> items = itemStorage.values();
        for (Item i : items) {
            if (i.getOwner().equals(ownerId)) {
                ownerItems.add(i);
            }
        }
        return ownerItems;
    }

    @Override
    public Collection<Item> search(String text) {
        Collection<Item> items = itemStorage.values();
        Collection<Item> searchCollection = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item i : items) {
                if (i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())
                        && i.getAvailable().equals(true)) {
                    searchCollection.add(i);
                }
            }
        }
        return searchCollection;
    }
}
