package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public final class RequestMapper {

    private RequestMapper() {
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto, User user) {

        return ItemRequest.builder()
                .requestId(requestDto.getId())
                .description(requestDto.getDescription())
                .requestor(user)
                .created(requestDto.getCreated())
                .build();
    }

    public static ItemRequestDto toRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getRequestId())
                .description(request.getDescription())
                .requestorId(request.getRequestor().getUserId())
                .created(request.getCreated())
                .build();
    }

    public static List<ItemRequestDto> toRequestDtoList(List<ItemRequest> requests) {
        List<ItemRequestDto> requestDtoList = new ArrayList<>();

        for (ItemRequest r : requests) {
            requestDtoList.add(toRequestDto(r));
        }
        return requestDtoList;
    }

}
