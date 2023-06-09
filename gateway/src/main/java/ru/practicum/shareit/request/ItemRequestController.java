package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid ItemRequestRequestDto requestDto) {
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "20") int size) {
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @PathVariable Long requestId) {
        return requestClient.getRequest(userId, requestId);
    }
}
