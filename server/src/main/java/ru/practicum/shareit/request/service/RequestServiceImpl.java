package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableUtil;
import ru.practicum.shareit.exception.CustomServerErrorException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Long requestorId) {
        User user = userRepository.findById(requestorId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Пользователь не найден");
        });
        requestDto.setCreated(LocalDateTime.now());
        ItemRequest request = requestRepository.save(RequestMapper.toItemRequest(requestDto, user));
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public Collection<ItemRequestDto> getUserRequests(Long requestorId) {
        User user = userRepository.findById(requestorId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Пользователь не найден");
        });

        List<ItemRequestDto> requests = RequestMapper.toRequestDtoList(
                requestRepository.findAllByRequestorUserId(requestorId));
        for (ItemRequestDto r : requests) {
            r.setItems(itemsForRequest(RequestMapper.toItemRequest(r, user)));
        }

        return requests;
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(int from, int size, Long userId) {
        Pageable pageable = PageableUtil.pageManager(from, size, "created");

        List<ItemRequestDto> requests = RequestMapper.toRequestDtoList(requestRepository
                .findAllExceptOwners(userId, pageable));

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomServerErrorException("Запрос от несуществующего пользователя");
        });
        for (ItemRequestDto r : requests) {
            r.setItems(itemsForRequest(RequestMapper.toItemRequest(r, user)));
        }
        return requests;
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomServerErrorException("Запрос от несуществующего пользователя");
        });

        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Request не найден");
        });

        ItemRequestDto requestDto = RequestMapper.toRequestDto(request);
        requestDto.setItems(itemsForRequest(request));
        return requestDto;
    }

    private List<ItemDto> itemsForRequest(ItemRequest request) {
        return ItemMapper.toItemDtoList(itemRepository.findAllByRequestId(request.getRequestId()));
    }
}