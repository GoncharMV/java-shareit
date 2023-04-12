package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public ItemDto add() {
        return null;
    }

    @Override
    public ItemDto edit() {
        return null;
    }

    @Override
    public ItemDto get(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getOwnerItems(Long ownerId) {
        return null;
    }
}
