package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto addRequest(ItemRequestDto request, Long requestorId);

    Collection<ItemRequestDto> getUserRequests(Long requestorId);

    Collection<ItemRequestDto> getAllRequests(int from, int size, Long userId);

    ItemRequestDto getRequestById(Long requestId, Long userId);
}
