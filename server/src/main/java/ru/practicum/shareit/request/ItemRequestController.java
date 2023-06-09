package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader(name = USER_ID_HEADER) Long requestorId,
                                      @RequestBody ItemRequestDto request) {
        return requestService.addRequest(request, requestorId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getUserRequests(@RequestHeader(name = USER_ID_HEADER) Long requestorId) {
        return requestService.getUserRequests(requestorId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                     @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @RequestParam(name = "size", defaultValue = "20") int size) {
        return requestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(name = USER_ID_HEADER) Long userId,
            @PathVariable Long requestId) {
        return requestService.getRequestById(requestId, userId);
    }

}
