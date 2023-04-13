package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // Привет! Когда делала назрел вопрос: где лучше использовать маппер, сразу в контроллере, в сервисе или в репозитории? Я сделала в сервисе, потому что это как бы "перевалочный пункт" между пользователем и программой, но как лучше?
    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        isOwnerExists(ownerId);
        Item item = ItemMapper.toItem(itemDto);
        itemRepository.add(item, ownerId);
        return ItemMapper.toItemDto(itemRepository.getItem(item.getId()));
    }

    @Override
    public ItemDto editItem(ItemDto item, Long ownerId, Long itemId) {
        isOwnerExists(ownerId);
        if (!itemRepository.getItem(itemId).getOwner().equals(ownerId)) {
            throw new ObjectNotFoundException("Редактировать может только владелец");
        }
        return ItemMapper.toItemDto(itemRepository.edit(ItemMapper.toItem(item), itemId));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItem(itemId));
    }

    @Override
    public Collection<ItemDto> getOwnerItems(Long ownerId) {
        Collection<Item> items = itemRepository.getOwnerItems(ownerId);
        Collection<ItemDto> itemDtos = new ArrayList<>();
        for (Item i : items) {
            itemDtos.add(ItemMapper.toItemDto(i));
        }
        return itemDtos;
    }

    @Override
    public Collection<ItemDto> search(String text) {
        Collection<Item> items = itemRepository.search(text);
        Collection<ItemDto> itemDtos = new ArrayList<>();
        for (Item i : items) {
            itemDtos.add(ItemMapper.toItemDto(i));
        }
        return itemDtos;
    }

    private void isOwnerExists(Long id) {
        if (userRepository.getUser(id) == null) {
            throw new ObjectNotFoundException("Пользователя несуществует");
        }
    }
}