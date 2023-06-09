package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                          @RequestBody @Valid ItemRequestDto requestDto) {
        return itemClient.addItem(userId, requestDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                          @PathVariable Long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                           @RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String text,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                              @RequestBody @Valid CommentRequestDto requestDto,
                                              @PathVariable Long itemId) {
        return itemClient.postComment(userId, requestDto, itemId);
    }

}
